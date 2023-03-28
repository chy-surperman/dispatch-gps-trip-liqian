package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.GpsTemplate;
import com.doudou.dispatch.trip.api.entities.LineInfo;
import com.doudou.dispatch.trip.api.entities.RouteWorkplanInfo;
import com.doudou.dispatch.trip.api.mappers.RouteGpsTemplateMapper;
import com.doudou.dispatch.trip.api.services.GpsTemplateService;
import com.doudou.dispatch.trip.api.services.RouteWorkplanInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 */
@Service("gpsTemplateService")
public class GpsTemplateServiceImpl implements GpsTemplateService {

    public static final int BUSY = 2;
    public static final int IDLE = 1;

    @Autowired
    private RouteGpsTemplateMapper routeGpsTemplateMapper;

    private Map<String,List<GpsTemplate>> gpsTemplateMap = new ConcurrentHashMap<>();

    @Autowired
    private RouteWorkplanInfoService routeWorkplanInfoService;

    @Override
    public List<GpsTemplate> getGpsTemplate(String routeName, int runLongTime) {
        //查询线路信息
        LineInfo lineInfo = routeWorkplanInfoService.getLineInfo(routeName);
        int timeLong = this.getStandTimeLong(lineInfo, runLongTime);

        List<GpsTemplate> gpsTemplates = gpsTemplateMap.get(this.getHashKey(routeName, timeLong));
        if(null != gpsTemplates){
            return gpsTemplates;
        }

        List<GpsTemplate> idleGpsList = routeGpsTemplateMapper.selectByRouteAndType(routeName, IDLE);
        gpsTemplateMap.put(this.getHashKey(routeName,idleGpsList.size() * 10 / 60),idleGpsList);

        List<GpsTemplate> busyGpsList = routeGpsTemplateMapper.selectByRouteAndType(routeName, BUSY);
        gpsTemplateMap.put(this.getHashKey(routeName,busyGpsList.size() * 10 / 60),busyGpsList);

        return gpsTemplateMap.get(this.getHashKey(routeName, timeLong));
    }

    @Override
    public List<GpsTemplate> getGpsTemplate(Workplan workplan) {
        if (workplan.getReportSiteCount() > 0 && StringUtils.isNotEmpty(workplan.getSiteName())){
            return routeGpsTemplateMapper.selectByRouteAndType(workplan.getSiteName(),workplan.getReportSiteCount());
        }

        RouteWorkplanInfo workplanInfo = routeWorkplanInfoService.getRouteWorkplanInfo(workplan);
        if(null != workplanInfo){
            return routeGpsTemplateMapper.selectByRouteAndType(workplan.getRouteName(),Integer.parseInt(workplanInfo.getType()));
        }
        //计算运行时长
        Date arrival = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getArrivalString(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date departure = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getDepartureString(),DateStyle.YYYY_MM_DD_HH_MM_SS);

        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长
        return this.getGpsTemplate(workplan.getRouteName(),runTime / 60);
    }

    public String getHashKey(String routeName,int timeLong){
        return routeName + "_" + timeLong;
    }

    private int getStandTimeLong(LineInfo lineInfo,int runLongTime){
        int[] timeArray = lineInfo.getRunTime();

        int max = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < timeArray.length; i++) {
            int value = Math.abs(timeArray[i] - runLongTime);
            if(value < max){
                max = value;
                index = i;
            }
        }
        return timeArray[index];
    }

    private int getTimeLong(LineInfo lineInfo,int runLongTime){
        int[] timeArray = lineInfo.getRunTime();
        if(timeArray.length == 1){
            return timeArray[0];
        }
        int[] absArray = new int[]{Math.abs(runLongTime - timeArray[0]),Math.abs(runLongTime - timeArray[1])};
        return absArray[0] < absArray[1] ? timeArray[0] : timeArray[1];
    }

}
