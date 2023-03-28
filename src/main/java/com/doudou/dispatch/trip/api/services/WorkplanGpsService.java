package com.doudou.dispatch.trip.api.services;



import com.dispatch.gps.commons.entities.GenerateGps;

import java.util.Date;
import java.util.List;

public interface WorkplanGpsService {

    public String saveWorkplanGps(Date date, List<GenerateGps> generateGps);

    public boolean confireWorkplanGps(Date date, String sign);

    public boolean cancelWorkplanGps(Date date, String sign);

    public String moveSlaveGps(String startDate,String endDate,String vehicleId);
}
