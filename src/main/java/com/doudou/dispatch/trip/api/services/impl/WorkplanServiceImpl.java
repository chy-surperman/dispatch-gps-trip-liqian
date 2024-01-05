package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.consts.Consts;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.dispatch.gps.commons.utils.GPSUtil;
import com.doudou.dispatch.trip.api.entities.*;
import com.doudou.dispatch.trip.api.feigns.GpsFeignClient;
import com.doudou.dispatch.trip.api.mappers.WorkplanMapper;
import com.doudou.dispatch.trip.api.services.GpsGenerateService;
import com.doudou.dispatch.trip.api.services.WorkplanGpsService;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import com.doudou.dispatch.trip.commons.HttpRespResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service("workplanService")
public class WorkplanServiceImpl implements WorkplanService {

    @Autowired
    private WorkplanMapper workplanMapper;

    @Autowired
    private WorkplanGpsService workplanGpsService;

    @Autowired
    private GpsFeignClient gpsFeignClient;

    @Autowired
    private GpsGenerateService gpsGenerateService;

    @Override
    public List<TVehicle> queryWorkplanVehicles(String date, String routeName) {
        return workplanMapper.selectWorkplanVehicles(routeName,date);
    }

    @Override
    public List<Workplan> queryWorkplans(QueryWorkplan queryWorkplan) {
        List<Workplan> workplans = workplanMapper.selectByQueryWorkplan(queryWorkplan);
        return this.formatWorkplans(workplans);
    }

    @Override
    public List<GpsLocus> getWorkplanGps(String startDate, String endDate, String vehicleId, String routeName) {
        HttpRespResult<List<GpsLocus>> master = gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "gpsDB");
        if (master.getCode() == HttpApiConstans.success) {
            if (null != master.getResult() && master.getResult().size() > 0) {
                return master.getResult();
            }
        }

        HttpRespResult<List<GpsLocus>> slave = gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "slaveGpsDB");
        if (slave.getCode() == HttpApiConstans.success) {
            if (null != slave.getResult() && slave.getResult().size() > 0) {
                return slave.getResult();
            }
        }
        return null;
    }

    @Override
    public boolean saveCreateWorkplanGps(CreateWorkplan workplan) {
        String startDate = workplan.getDateString() + " " + workplan.getDepartureString();
        String endDate = workplan.getDateString() + " " + workplan.getArrivalString();
        String vehicleId = workplan.getVehicleId();
        String routeName = workplan.getRouteName();
        String starttag = workplan.getStarttag();

        HttpRespResult<List<GpsLocus>> master = gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "gpsDB");
        if (master.getCode() == HttpApiConstans.success) {
            if (null != master.getResult() && master.getResult().size() > 0) {
                return true;
            }
        }

        HttpRespResult<List<GpsLocus>> slave = gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "slaveGpsDB");
        if (slave.getCode() == HttpApiConstans.success) {
            if (null != slave.getResult() && slave.getResult().size() > 0) {
                List<GpsLocus> gpsLocusList = slave.getResult();
                double mileage = this.calMileage(gpsLocusList);
                if(mileage >= 5){   //里程大于五公里则使用从库GPS
                    //将从库GPS移到主库
                    String sign = workplanGpsService.moveSlaveGps(startDate, endDate, vehicleId);
                    if(StringUtils.isNotEmpty(sign)){
                        workplan.setGpsSign(sign);
                        return true;
                    }
                    return false;
                }
            }
        }
        //没有GPS，需要创造GPS
        List<GenerateGps> generateGpss=gpsGenerateService.generateGps(startDate, endDate, vehicleId, routeName, starttag);
        Date date = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
        String sign = workplanGpsService.saveWorkplanGps(date, generateGpss);
        if(StringUtils.isNotEmpty(sign)){
            workplan.setGpsSign(sign);
            return true;
        }

        return false;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public int saveManulWorkplanChangeRecode(WorkplanChangeRecode changeRecord) throws Exception {
        CreateWorkplan associationWorkplan = changeRecord.getAssociationWorkplan();

        // 设置趟次是第几轮
        List<Workplan> workplans = workplanMapper.selectDriverWorkplans(associationWorkplan.getDriverId(),
                associationWorkplan.getDateString());
        float trip = workplans.size() + 1;

        associationWorkplan.setTrip(workplans.size() / 2f + 0.5f);

        if (null == associationWorkplan.getId() || associationWorkplan.getId().longValue() == 0) {

            associationWorkplan.setStatus(Consts.WORK_PLAN_STATUS_ARRIVAL);
            associationWorkplan.setDepartureWay(Consts.DEPARTURE_WAY_MANUAL);
            associationWorkplan.setArrivalWay(Consts.ARRIVAL_WAY_MANUAL);
            associationWorkplan.setIsThroughUpMonitorSite(1);
            associationWorkplan.setIsThroughDownMonitorSite(1);
            //先保存GPS
            if(!this.saveCreateWorkplanGps(associationWorkplan)){
                throw new Exception("添加失败");
            }

            if (workplanMapper.insert(associationWorkplan) < 1) {
                throw new Exception("添加失败");
            }
            changeRecord.setWorkplanId(associationWorkplan.getId());
        } else {
            // 手动更新一条全新的排班记录
            CreateWorkplan dbWorkplan = workplanMapper.selectById(changeRecord.getAssociationWorkplan().getId());
            changeRecord.setWorkplanId(dbWorkplan.getId());

            if (StringUtils.isNotBlank(dbWorkplan.getDriverId())
                    && !dbWorkplan.getDriverId().equals(associationWorkplan.getDriverId())) {
                return -1;
            }

            associationWorkplan.setStatus(Consts.WORK_PLAN_STATUS_ARRIVAL);
            associationWorkplan.setTrip(dbWorkplan.getTrip());
            associationWorkplan.setIsThroughUpMonitorSite(1);
            associationWorkplan.setIsThroughDownMonitorSite(1);
            //如果生成过GPS，则删除
            if(StringUtils.isNotEmpty(dbWorkplan.getGpsSign())){
                if(!workplanGpsService.cancelWorkplanGps(dbWorkplan.getDate(),dbWorkplan.getGpsSign())){
                    throw new Exception("添加失败");
                }
            }
            //先保存GPS
            if(!this.saveCreateWorkplanGps(associationWorkplan)){
                throw new Exception("添加失败");
            }
            if (workplanMapper.update(associationWorkplan) < 1) {
                throw new Exception("添加失败");
            }
        }
        return 1;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public int saveManulWorkplanChangeRecodeWithoutGPS(WorkplanChangeRecode changeRecord) throws Exception {
        CreateWorkplan associationWorkplan = changeRecord.getAssociationWorkplan();

        // 设置趟次是第几轮
        List<Workplan> workplans = workplanMapper.selectDriverWorkplans(associationWorkplan.getDriverId(),
                associationWorkplan.getDateString());
        float trip = workplans.size() + 1;

        associationWorkplan.setTripValue(workplans.size() / 2f + 0.5f);

        if (null == associationWorkplan.getId() || associationWorkplan.getId().longValue() == 0) {

            associationWorkplan.setStatus(Consts.WORK_PLAN_STATUS_ARRIVAL);
            associationWorkplan.setDepartureWay(Consts.DEPARTURE_WAY_MANUAL);
            associationWorkplan.setArrivalWay(Consts.ARRIVAL_WAY_MANUAL);
            associationWorkplan.setIsThroughUpMonitorSite(1);
            associationWorkplan.setIsThroughDownMonitorSite(1);
            associationWorkplan.setTripValue(Float.valueOf("0.5"));

            if (workplanMapper.insert(associationWorkplan) < 1) {
                throw new Exception("添加失败");
            }
            changeRecord.setWorkplanId(associationWorkplan.getId());
        } else {
            // 手动更新一条全新的排班记录
            CreateWorkplan dbWorkplan = workplanMapper.selectById(changeRecord.getAssociationWorkplan().getId());
            changeRecord.setWorkplanId(dbWorkplan.getId());

            if (StringUtils.isNotBlank(dbWorkplan.getDriverId())
                    && !dbWorkplan.getDriverId().equals(associationWorkplan.getDriverId())) {
                return -1;
            }

            associationWorkplan.setStatus(Consts.WORK_PLAN_STATUS_ARRIVAL);
            associationWorkplan.setTrip(dbWorkplan.getTripValue());
            associationWorkplan.setIsThroughUpMonitorSite(1);
            associationWorkplan.setIsThroughDownMonitorSite(1);
            //如果生成过GPS，则删除
            if(StringUtils.isNotEmpty(dbWorkplan.getGpsSign())){
                if(!workplanGpsService.cancelWorkplanGps(dbWorkplan.getDate(),dbWorkplan.getGpsSign())){
                    throw new Exception("添加失败");
                }
            }
//            //先保存GPS
//            if(!this.saveCreateWorkplanGps(associationWorkplan)){
//                throw new Exception("添加失败");
//            }
            if (workplanMapper.update(associationWorkplan) < 1) {
                throw new Exception("添加失败");
            }
        }
        return 1;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean deleteWorkplan(long id) throws Exception {
        CreateWorkplan createWorkplan = workplanMapper.selectById(id);
        if(null == createWorkplan){
            return false;
        }
        if(workplanMapper.deleteById(id) < 0){
            throw new Exception("删除失败");
        }

        //如果没有gpsSign则不进行删除
        if(StringUtils.isEmpty(createWorkplan.getGpsSign())){
            return true;
        }

        if(!workplanGpsService.cancelWorkplanGps(createWorkplan.getDate(),createWorkplan.getGpsSign())){
            throw new Exception("删除失败");
        }
        return true;
    }



    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean deleteWorkplanWithoutGps(long id) throws Exception {
        CreateWorkplan createWorkplan = workplanMapper.selectById(id);
        if(null == createWorkplan){
            return false;
        }
        if(workplanMapper.deleteById(id) < 0){
            throw new Exception("删除失败");
        }

        //如果没有gpsSign则不进行删除
        if(StringUtils.isEmpty(createWorkplan.getGpsSign())){
            return true;
        }

        return true;
    }

    @Override
    public List<BigDailyWorkplanReport> getBigDailyWorkplanReport(String routeName, String date, String isPrint) {
        return null;
    }

    private List<Workplan> formatWorkplans(List<Workplan> workplans){
        for (Workplan plan : workplans) {
            if (null != plan.getScheduleTime() && null != plan.getDate()) {
                plan.setScheduleString(DateUtil.getDateByDs(plan.getScheduleTime(), DateStyle.HH_MM_SS));
                plan.setScheduleTimestamp(plan.getScheduleTime().getTime());
                try {
                    Date scheduleTime = DateUtil.getDate(
                            DateUtil.getDateByDs(plan.getDate(), DateStyle.YYYY_MM_DD) + " " + plan.getScheduleString(),
                            DateStyle.YYYY_MM_DD_HH_MM_SS);
                    plan.setDepartureTimeOut(
                            DateUtil.getUnixTimestamp() - new Long(scheduleTime.getTime() / 1000).intValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (null != plan.getDepartureTime())
                plan.setDepartureString(DateUtil.getDateByDs(plan.getDepartureTime(), DateStyle.HH_MM_SS));
            if (null != plan.getArrivalTime())
                plan.setArrivalString(DateUtil.getDateByDs(plan.getArrivalTime(), DateStyle.HH_MM_SS));
            if (null != plan.getDate()) {
                plan.setDateString(DateUtil.getDateByDs(plan.getDate(), DateStyle.YYYY_MM_DD));
            }
        }
        return workplans;
    }

    public double calMileage(List<GpsLocus> gpsLocus){
        double mileageTotal = 0;

        if (gpsLocus.size() >= 1) {
            for (int i = 1; i < gpsLocus.size(); i++) {

                GpsLocus gpsreport = gpsLocus.get(i);
                GpsLocus upGpsreport = gpsLocus.get(i - 1);

                double mileage = GPSUtil.getDistance(gpsreport.getLatitude(), gpsreport.getLongitude(),
                        upGpsreport.getLatitude(), upGpsreport.getLongitude());

                // 如果平均速度超过50m/s，认为漂移，过滤掉
                Date date = DateUtil.getDateByString(gpsreport.getReportTime(),DateStyle.YYYY_MM_DD_HH_MM_SS);
                Date upDate = DateUtil.getDateByString(upGpsreport.getReportTime(),DateStyle.YYYY_MM_DD_HH_MM_SS);
                if (mileage / (new Long(date.getTime() / 1000 - upDate.getTime() / 1000)).intValue() >= 50) {
                    continue;
                }

                mileageTotal += mileage;
            }
        }
        return mileageTotal  / 1000;
    }
}
