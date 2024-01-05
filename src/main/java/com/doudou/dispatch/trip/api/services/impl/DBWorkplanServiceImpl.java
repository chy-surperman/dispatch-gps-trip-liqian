package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.consts.Consts;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.entities.WorkplanPostGps;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.dispatch.gps.commons.utils.GPSUtil;
import com.doudou.dispatch.trip.api.entities.*;
import com.doudou.dispatch.trip.api.feigns.GpsFeignClient;
import com.doudou.dispatch.trip.api.feigns.WorkplanGpsFeignClient;
import com.doudou.dispatch.trip.api.mappers.GpsreportMapper;
import com.doudou.dispatch.trip.api.mappers.WorkplanMapper;
import com.doudou.dispatch.trip.api.services.*;
import com.doudou.dispatch.trip.commons.HttpRespResult;
import com.doudou.dispatch.trip.commons.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("dbWorkplanService")
public class DBWorkplanServiceImpl implements WorkplanService {
    @Autowired
    private WorkplanGpsService workplanGpsService;

    @Autowired
    private WorkplanMapper workplanMapper;

    @Autowired
    private PostGpsService postGpsService;

    @Autowired
    private GpsGenerateService gpsGenerateService;
    @Autowired
    private GpsFeignClient gpsFeignClient;
    @Autowired
    private WorkplanGpsFeignClient workplanGpsFeignClient;

    @Autowired
    private GpsreportMapper gpsreportMapper;

    @Autowired
    private GpsTableManagerService gpsTableManagerService;

    @Autowired
    private IdWorker idWorker;


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
//        Date start = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
//        Date end = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
//        return postGpsService.getVehicleLocus(start, end, vehicleId);
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

    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
    @Override
    public boolean saveCreateWorkplanGps(CreateWorkplan workplan) throws Exception {
        String startDate = workplan.getDateString() + " " + workplan.getDepartureString();
        String endDate = workplan.getDateString() + " " + workplan.getArrivalString();
        String vehicleId = workplan.getVehicleId();
        String routeName = workplan.getRouteName();
        String starttag = workplan.getStarttag();
        Date startTime = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date endTime = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);

        if (StringUtils.isNotEmpty(workplan.getCreateNewGps()) && "1".equals(workplan.getCreateNewGps())){
            //删除GPS
            String gpsTableName = gpsTableManagerService.getGpsTableName(startTime);
            System.out.println(startTime+"---"+endTime+"---"+vehicleId+"---"+gpsTableName);
            workplanGpsFeignClient.deleteGps(startDate,endDate,vehicleId,gpsTableName);
//            gpsreportMapper.deleteGps(gpsTableName, startTime, endTime, vehicleId);
        }
//
//        if (StringUtils.isEmpty(workplan.getCreateNewGps()) || "0".equals(workplan.getCreateNewGps())) {
//            if(workplan.getIsDeletedGps() != 1  //GPS已经删除
//                    || (StringUtils.isNotEmpty(workplan.getCreateNewGps()) && "1".equals(workplan.getCreateNewGps()))){
//                List<GpsLocus> gpsLocusList = postGpsService.getVehicleLocus(startTime,endTime,vehicleId);
//                if (null != gpsLocusList && gpsLocusList.size() > 0) {
//                    return true;
//                }
//            }
//        }
//
//        //没有GPS，需要创造GPS
//        List<GenerateGps>  generateGpss= gpsGenerateService.generateGps(startDate, endDate, vehicleId, routeName, starttag);
//        Date date = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
//
//        WorkplanPostGps workplanPostGps = new WorkplanPostGps();
//        workplanPostGps.setGpsLists(generateGpss);
//        workplanPostGps.setDate(DateUtil.getDateByDs(date, DateStyle.YYYY_MM_DD));
//        workplanPostGps.setSign(idWorker.nextId() + "");
//        if (!postGpsService.savePostGps(workplanPostGps)) {
//            throw new Exception("保存失败");
//        }
//        workplan.setGpsSign(workplanPostGps.getSign());
//
//        return true;
//        String startDate = workplan.getDateString() + " " + workplan.getDepartureString();
//        String endDate = workplan.getDateString() + " " + workplan.getArrivalString();
//        String vehicleId = workplan.getVehicleId();
//        String routeName = workplan.getRouteName();
//        String starttag = workplan.getStarttag();


        HttpRespResult<List<GpsLocus>> master = this.gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "gpsDB");
        if (master.getCode() == HttpApiConstans.success &&
                null != master.getResult() && ((List)master.getResult()).size() > 0)
            return true;
        HttpRespResult<List<GpsLocus>> slave = this.gpsFeignClient.getVehicleGps(startDate, endDate, vehicleId, 1, "slaveGpsDB");
        if (slave.getCode() == HttpApiConstans.success &&
                null != slave.getResult() && ((List)slave.getResult()).size() > 0) {
            List<GpsLocus> gpsLocusList = (List<GpsLocus>)slave.getResult();
            double mileage = calMileage(gpsLocusList);
            if (mileage >= 5.0D) {
                String str = this.workplanGpsService.moveSlaveGps(startDate, endDate, vehicleId);
                if (StringUtils.isNotEmpty(str)) {
                    workplan.setGpsSign(str);
                    return true;
                }
                return false;
            }
        }
        List<GenerateGps> generateGpss = this.gpsGenerateService.generateGps(startDate, endDate, vehicleId, routeName, starttag);
        Date date = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
        String sign = this.workplanGpsService.saveWorkplanGps(date, generateGpss);
        if (StringUtils.isNotEmpty(sign)) {
            workplan.setGpsSign(sign);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
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
//            String startDate = dbWorkplan.getDateString() + " " + dbWorkplan.getDepartureString();
//            String endDate = dbWorkplan.getDateString() + " " + dbWorkplan.getArrivalString();
//            String vehicleId = dbWorkplan.getVehicleId();
//            String routeName = dbWorkplan.getRouteName();
//            String starttag = dbWorkplan.getStarttag();
//            Date startTime = DateUtil.getDate(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
//            Date endTime = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
            String vehicleId = dbWorkplan.getVehicleId();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
            String startDate = formatter.format(dbWorkplan.getDate())+ " " +formatter2.format(dbWorkplan.getDepartureTime());
            String endDate = formatter.format(dbWorkplan.getDate())+ " " +formatter2.format(dbWorkplan.getArrivalTime());
            Date startTime = DateUtil.getDate(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
            Date endTime = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
            String gpsTableName = gpsTableManagerService.getGpsTableName(dbWorkplan.getDate());
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
                System.out.println(startTime+"---"+endTime+"---"+vehicleId+"---"+gpsTableName);
                if (workplanGpsFeignClient.deleteGps(startDate,endDate,vehicleId,gpsTableName).getCode()<0) {
                    throw new Exception("添加失败");
                }
                associationWorkplan.setIsDeletedGps(1);
                associationWorkplan.setGpsSign(dbWorkplan.getGpsSign());
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

    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
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

    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteWorkplan(long id) throws Exception {
        CreateWorkplan createWorkplan = this.workplanMapper.selectById(id);
        if (null == createWorkplan)
            return false;
        if (this.workplanMapper.deleteById(id) < 0)
            throw new Exception("");
        if (StringUtils.isEmpty(createWorkplan.getGpsSign()))
            return true;
        if (!this.workplanGpsService.cancelWorkplanGps(createWorkplan.getDate(), createWorkplan.getGpsSign()))
            throw new Exception("");
        return true;
//
//        CreateWorkplan createWorkplan = workplanMapper.selectById(id);
//        if (null == createWorkplan) {
//            return false;
//        }
//        if (workplanMapper.deleteById(id) < 0) {
//            throw new Exception("删除失败");
//        }
//
//        //如果没有gpsSign则不进行删除
//        if (StringUtils.isEmpty(createWorkplan.getGpsSign())) {
//            return true;
//        }
//
////        if (!postGpsService.cancelPostGps(DateUtil.getDateByDs(createWorkplan.getDate(), DateStyle.YYYY_MM_DD), createWorkplan.getGpsSign())) {
////            throw new Exception("删除失败");
////        }
//
//        return true;
    }


    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteWorkplanWithoutGps(long id) throws Exception {
        CreateWorkplan createWorkplan = workplanMapper.selectById(id);
        if (null == createWorkplan) {
            return false;
        }
        if (workplanMapper.deleteById(id) < 0) {
            throw new Exception("删除失败");
        }

        //如果没有gpsSign则不进行删除
        if (StringUtils.isEmpty(createWorkplan.getGpsSign())) {
            return true;
        }
        return true;
    }



    @Override
    public List<BigDailyWorkplanReport> getBigDailyWorkplanReport(String routeName, String date, String isPrint) {
        List<BigDailyWorkplanReport> workplanReports = new ArrayList<BigDailyWorkplanReport>();
        Map<String, BigDailyWorkplanReport> dailyReportMap = new HashMap<String, BigDailyWorkplanReport>();

        List<BigDailyWorkplan> workplans = workplanMapper.selectBigDailyWorkplanReport(routeName, date,isPrint);

        Date date289 = DateUtil.getDateByString("2020-04-25", DateStyle.YYYY_MM_DD);
        Date d = DateUtil.getDateByString(date,DateStyle.YYYY_MM_DD);

        for (BigDailyWorkplan workplan : workplans) {
            if(routeName.equals("289") && d.compareTo(date289) <= 0){
                if (Consts.STARTTAG_UP.equals(workplan.getStarttag())) {
                    workplan.setStarttag("" + workplan.getSelfNum() + "");
                } else {
                    workplan.setStarttag("" + workplan.getSelfNum() + "");
                }
            }else{
                if (Consts.STARTTAG_UP.equals(workplan.getStarttag())) {
                    workplan.setStarttag("" + workplan.getSelfNum() + "");
                } else {
                    workplan.setStarttag("" + workplan.getSelfNum() + "");
                }
            }


            BigDailyWorkplanReport report = dailyReportMap.get(workplan.getDriverName());
            if (null == report) {
                report = new BigDailyWorkplanReport();
                report.setDriverName(workplan.getDriverName());
                report.setSelfNums(workplan.getSelfNum());

                List<BigDailyWorkplan> reportWorkplan = new ArrayList<BigDailyWorkplan>();
                report.setWorkplans(reportWorkplan);

                dailyReportMap.put(workplan.getDriverName(), report);
                workplanReports.add(report);
            }
            if(StringUtils.isNotEmpty(report.getSelfNums())){
                if (report.getSelfNums().indexOf(workplan.getSelfNum()) == -1) {
                    String selfs = report.getSelfNums() + "," + workplan.getSelfNum();
                    report.setSelfNums(selfs);
                }
            }

            report.getWorkplans().add(workplan);
        }

        return workplanReports;
    }

    private List<Workplan> formatWorkplans(List<Workplan> workplans) {
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

    public double calMileage(List<GpsLocus> gpsLocus) {
        double mileageTotal = 0;

        if (gpsLocus.size() >= 1) {
            for (int i = 1; i < gpsLocus.size(); i++) {

                GpsLocus gpsreport = gpsLocus.get(i);
                GpsLocus upGpsreport = gpsLocus.get(i - 1);

                double mileage = GPSUtil.getDistance(gpsreport.getLatitude(), gpsreport.getLongitude(),
                        upGpsreport.getLatitude(), upGpsreport.getLongitude());

                // 如果平均速度超过50m/s，认为漂移，过滤掉
                Date date = DateUtil.getDateByString(gpsreport.getReportTime(), DateStyle.YYYY_MM_DD_HH_MM_SS);
                Date upDate = DateUtil.getDateByString(upGpsreport.getReportTime(), DateStyle.YYYY_MM_DD_HH_MM_SS);
                if (mileage / (new Long(date.getTime() / 1000 - upDate.getTime() / 1000)).intValue() >= 50) {
                    continue;
                }

                mileageTotal += mileage;
            }
        }
        return mileageTotal / 1000;
    }
}
