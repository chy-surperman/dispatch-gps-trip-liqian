package com.doudou.dispatch.trip.api.entities;

import java.util.List;

public class BigDailyWorkplanReport {
	private String driverName;
	private String selfNums;
	private List<BigDailyWorkplan> workplans;
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getSelfNums() {
		return selfNums;
	}
	public void setSelfNums(String selfNums) {
		this.selfNums = selfNums;
	}
	public List<BigDailyWorkplan> getWorkplans() {
		return workplans;
	}
	public void setWorkplans(List<BigDailyWorkplan> workplans) {
		this.workplans = workplans;
	}
	
	
}
