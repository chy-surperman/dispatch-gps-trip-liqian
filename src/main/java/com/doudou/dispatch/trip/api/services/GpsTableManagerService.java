package com.doudou.dispatch.trip.api.services;

import java.util.Date;

public interface GpsTableManagerService {

	public String getGpsTableName();
	
	public String getGpsTableName(Date d);
	
	public String getGpsTableName(String date);
}
