package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.entities.GenerateGps;

import java.util.Date;
import java.util.List;

public interface QueryGpsService {
    public List<GenerateGps> queryGps(String routeName, String startTime, String endTime, String vehicleId, Date date);
}
