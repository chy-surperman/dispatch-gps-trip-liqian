package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.doudou.dispatch.trip.api.services.QueryGpsService;
import com.doudou.dispatch.trip.api.services.WorkplanGpsGenerateService;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("queryGpsService")
public class QueryGpsServiceImpl implements QueryGpsService {

    @Qualifier("dbWorkplanService")
    @Autowired
    private WorkplanService workplanService;

    @Autowired
    private WorkplanGpsGenerateService workplanGpsGenerateService;

    @Override
    public List<GenerateGps> queryGps(String routeName, String startTime, String endTime, String vehicleId, Date date) {
        //可能查出多个排班
        List<Workplan> workplans = this.getWorkplans(routeName, startTime, endTime, vehicleId, date);
        if(null == workplans || workplans.size() == 0){
            return null;
        }

        List<List<GenerateGps>> genGpsLists = new ArrayList<>();
        for (int i = 0; i < workplans.size(); i++) {
            genGpsLists.add(workplanGpsGenerateService.generateGps(workplans.get(i)));
        }

        Date start = DateUtil.getDateByString(startTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date end = DateUtil.getDateByString(endTime, DateStyle.YYYY_MM_DD_HH_MM_SS);

        List<GenerateGps> generateGpsList = new ArrayList<>();
        for (int i = 0; i < genGpsLists.size(); i++) {
            List<GenerateGps> gpsList = genGpsLists.get(i);
            for (GenerateGps gps:gpsList) {
                if(gps.getCreateTime().compareTo(start) >=0 && gps.getCreateTime().compareTo(end) <= 0){
                    generateGpsList.add(gps);
                }
            }
        }
        return generateGpsList;
    }

    public List<Workplan> getWorkplans(String routeName,String startTime, String endTime, String vehicleId, Date date){
        QueryWorkplan queryWorkplan = new QueryWorkplan();
        queryWorkplan.setDate(DateUtil.getDateByDs(date,DateStyle.YYYY_MM_DD));
        queryWorkplan.setVehicleId(vehicleId);
        queryWorkplan.setRouteName(routeName);
        List<Workplan> workplans = workplanService.queryWorkplans(queryWorkplan);

        Date start = DateUtil.getDateByString(startTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date end = DateUtil.getDateByString(endTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
        return this.getWorkplans(workplans,start,end);
    }

    public List<Workplan> getWorkplans(List<Workplan> workplans,Date start,Date end) {

        List<Workplan> queryWorklans = new ArrayList<>(workplans.size());
        for(Workplan workplan:workplans){
            Date departure = this.getWorkplanDepartue(workplan);
            Date arrive = this.getWorkplanArrive(workplan);
            //如果起始时间在排班范围内
            if(start.compareTo(departure) >= 0 && start.compareTo(arrive) <= 0){
                queryWorklans.add(workplan);
                continue;
            }
            if (end.compareTo(departure) >=0 && end.compareTo(arrive) <= 0) {
                queryWorklans.add(workplan);
                continue;
            }
            if(start.compareTo(departure) <= 0 && arrive.compareTo(end) <= 0){
                queryWorklans.add(workplan);
                continue;
            }
        }
        return queryWorklans;
    }

    private Date getWorkplanDepartue(Workplan workplan){
        return DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getDepartureString(), DateStyle.YYYY_MM_DD_HH_MM_SS);
    }

    private Date getWorkplanArrive(Workplan workplan){
        return DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getArrivalString(),DateStyle.YYYY_MM_DD_HH_MM_SS);
    }
}
