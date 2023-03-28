package com.doudou.dispatch.trip.api.services;


import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.LineInfo;
import com.doudou.dispatch.trip.api.entities.RouteWorkplanInfo;

public interface RouteWorkplanInfoService {

    public final static int GPS_REPORT_FREQ = 10;

    public final static int GPS_REPORT_FREQ_28 = 28;

    public int[] getRunTime(String routeName);

    public LineInfo getLineInfo(String routeName);

    public RouteWorkplanInfo getRouteWorkplanInfo(Workplan workplan);
}
