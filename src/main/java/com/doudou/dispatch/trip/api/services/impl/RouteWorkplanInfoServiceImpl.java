package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.LineInfo;
import com.doudou.dispatch.trip.api.entities.RouteWorkplanInfo;
import com.doudou.dispatch.trip.api.mappers.LineInfoMapper;
import com.doudou.dispatch.trip.api.mappers.RouteWorkplanInfoMapper;
import com.doudou.dispatch.trip.api.services.RouteWorkplanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service("routeWorkplanInfoService")
public class RouteWorkplanInfoServiceImpl implements RouteWorkplanInfoService {

    private List<RouteWorkplanInfo> workplanInfos;

    private Map<String, LineInfo> lineInfoMap = new ConcurrentHashMap<>();

    @Autowired
    private LineInfoMapper lineInfoMapper;

    @Autowired
    private RouteWorkplanInfoMapper routeWorkplanInfoMapper;

    private Random random = new Random();

    @PostConstruct
    public void initWorkplanInfos(){
        List<LineInfo> lineInfos = lineInfoMapper.selectLineInfo();
        for(LineInfo lineInfo:lineInfos){
            lineInfoMap.put(lineInfo.getRouteName(),lineInfo);
        }

        //计算一趟时间
        List<RouteWorkplanInfo> routeWorkplanInfos = routeWorkplanInfoMapper.selectRouteWorkplanInfo(GPS_REPORT_FREQ);
        this.workplanInfos = routeWorkplanInfos;
        for (int i = 0; i < routeWorkplanInfos.size(); i++) {
            RouteWorkplanInfo workplanInfo = routeWorkplanInfos.get(i);
            LineInfo lineInfo = lineInfoMap.get(workplanInfo.getRouteName());
            if(null != lineInfo)
                workplanInfo.setRunTime((workplanInfo.getGpsSize() * lineInfo.getGpsFreq()) / 60);
        }
    }

    @Override
    public int[] getRunTime(String routeName) {
        if(null == workplanInfos){
            this.initWorkplanInfos();
        }
        List<RouteWorkplanInfo> routeWorkplanInfos = workplanInfos.stream().filter(ele -> ele.getRouteName().equals(routeName)).collect(Collectors.toList());
        routeWorkplanInfos.sort(new Comparator<RouteWorkplanInfo>() {
            @Override
            public int compare(RouteWorkplanInfo o1, RouteWorkplanInfo o2) {
                return new Integer(o1.getRunTime()).compareTo(o2.getRunTime());
            }
        });

        int[] runTime = new int[routeWorkplanInfos.size()];
        for (int i = 0; i < runTime.length; i++) {
            runTime[i] = routeWorkplanInfos.get(i).getRunTime();
        }
        return runTime;
    }

    @Override
    public LineInfo getLineInfo(String routeName) {
        LineInfo lineInfo = this.lineInfoMap.get(routeName);
        if(null == lineInfo){
            return null;
        }
        lineInfo.setRunTime(this.getRunTime(routeName));
        return lineInfo;
    }

    @Override
    public RouteWorkplanInfo getRouteWorkplanInfo(Workplan workplan) {
        List<RouteWorkplanInfo> workplanInfos = this.getRouteWorkplanInfo(workplan.getRouteName());
        return this.findWorkplanInfo(workplanInfos,workplan);
    }

    public List<RouteWorkplanInfo> getRouteWorkplanInfo(String routeName) {

        List<RouteWorkplanInfo> routeWorkplanInfos = workplanInfos.stream().filter(ele -> ele.getRouteName().equals(routeName)).collect(Collectors.toList());
        routeWorkplanInfos.sort(new Comparator<RouteWorkplanInfo>() {
            @Override
            public int compare(RouteWorkplanInfo o1, RouteWorkplanInfo o2) {
                return new Integer(o1.getRunTime()).compareTo(o2.getRunTime());
            }
        });
        return routeWorkplanInfos;
    }

    private RouteWorkplanInfo findWorkplanInfo(List<RouteWorkplanInfo> workplanInfos, Workplan workplan) {
        //计算运行时长
        Date arrival = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getArrivalString(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date departure = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getDepartureString(),DateStyle.YYYY_MM_DD_HH_MM_SS);

        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000 / 60).intValue();//运行时长

        int max = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < workplanInfos.size(); i++) {
            RouteWorkplanInfo workplanInfo = workplanInfos.get(i);
            if(Math.abs(runTime - workplanInfo.getRunTime()) < max){
                max = Math.abs(runTime - workplanInfo.getRunTime());
                index = i;
            }
        }
        return workplanInfos.get(index);
    }

}
