package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.Workplan;

import java.util.List;

public interface WorkplanGpsGenerateService {

    public static String[] gpsFreq28RouteName = new String[]{"X118","城乡1号线","X118延长线","X111"};

    public List<GenerateGps> generateGps(Workplan workplan);

    public int getGpsFreq(String routeName);

}
