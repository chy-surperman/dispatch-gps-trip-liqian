package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.services.GpsTableManagerService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("gpsTableManagerService")
public class GpsTableManagerServiceImpl implements GpsTableManagerService {

	private static String dbName = "gpsDB";

	private static String slaveDbName = "slaveGpsDB";

	public String getGpsTableName() {
		return "gpsreport_" + DateUtil.getDateByDs(new Date(), DateStyle.YYYYMMDD);
	}

	public String getGpsTableName(Date d) {
		return "gpsreport_" + DateUtil.getDateByDs(d, DateStyle.YYYYMMDD);
	}

	public String getGpsTableName(String date) {
		date = date.replaceAll("-", "");
		return "gpsreport_" + date;
	}

}
