package com.doudou.dispatch.trip.api.services;


import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.entities.WorkplanPostGps;

import java.util.Date;
import java.util.List;

public interface PostGpsService {
	
	public boolean savePostGps(WorkplanPostGps workplanPostGps);
	
	public boolean confirmPostGps(String date,String sign);
	
	public boolean cancelPostGps(String date,String sign);
	
	public boolean moveSlaveGps(String startDate,String endDate,String vehicleId,String sign);

	public boolean deleteGps(Date startTime, Date endTime, String vehicleId);

	public List<GpsLocus> getVehicleLocus(Date startTime, Date endTime, String vehicleId);
}
