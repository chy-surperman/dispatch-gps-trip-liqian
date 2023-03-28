package com.doudou.dispatch.trip.api.services;


import com.dispatch.gps.commons.entities.GenerateGps;

import java.util.List;

public interface GpsGenerateService {

    public static final int BUSY = 2;
    public static final int IDLE = 1;

    public List<GenerateGps> generateGps(String startDate, String endDate, String vehicleId, String routeName, String starttag);

    public int getRouteRunTime(String routeName,String starttag);
}
