package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class BigDailyWorkplan {
	private String driverName;
	private String plateNum;
	private String routeName;
	private String departureString;
	private String arrivalString;
	private String status;
	private String starttag;
	private int departureWay;
	private int arrivalWay;
	private String selfNum;
	private int isThroughUpMonitorSite;
}
