package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.doudou.dispatch.trip.api.services.GernalTripLocusService;
import com.doudou.dispatch.trip.api.services.GpsGenerateService;
import com.doudou.dispatch.trip.api.services.WorkplanGpsService;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <b><code>GernalTripLocusServiceImp</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2024/7/10 15:56.
 *
 * @author xxx
 * @since dispatch-gps-trip Chy
 */
@Service("GernalTripLocusService")
public class GernalTripLocusServiceImp implements GernalTripLocusService {
    @Autowired
    public WorkplanService workplanService;


    @Autowired
    private GpsGenerateService gpsGenerateService;


    @Autowired
    private WorkplanGpsService workplanGpsService;

    @Override
    public void CreateGpsLocus(String date,String routeName,String vehicleId) throws ParseException {
        QueryWorkplan queryWorkplan = new QueryWorkplan(date,routeName,vehicleId);
        List<Workplan> workplans = workplanService.queryWorkplans(queryWorkplan);
        if (workplans!=null){
            for (Workplan workplan:workplans){
                //1.获取线路名 发车时间 到达时间
                workplan.setDate(DateUtil.getDate(workplan.getDateString(), DateStyle.YYYY_MM_DD));
                workplan.setArrivalTime(DateUtil.getDate(workplan.getArrivalString(), DateStyle.HH_MM_SS));
                workplan.setDepartureTime(DateUtil.getDate(workplan.getDepartureString(), DateStyle.HH_MM_SS));
                String startDate = workplan.getDateString() + " " + workplan.getDepartureString();
                String endDate = workplan.getDateString() + " " + workplan.getArrivalString();
                String vehicleIdParam = workplan.getVehicleId();
                String routeNameParam = workplan.getRouteName();
                String starttagParam = workplan.getStarttag();
                Date startTime = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                Date endTime = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                //2.生成轨迹
                List<GenerateGps> generateGpss = this.gpsGenerateService.generateGps(startDate, endDate, vehicleIdParam, routeNameParam, starttagParam);
                Date dateParam = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
                String sign = this.workplanGpsService.saveWorkplanGps(dateParam, generateGpss);
                System.out.println(sign);
            }
        }
    }
}
