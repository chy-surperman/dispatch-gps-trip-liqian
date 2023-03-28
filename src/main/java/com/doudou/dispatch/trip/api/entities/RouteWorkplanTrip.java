package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

import java.util.List;

@Data
public class RouteWorkplanTrip {
	private String routeName;
	private String groupName;
	private List<DriverTrip> workplanTrips;

	public RouteWorkplanTrip(String routeName, List<DriverTrip> workplanTrips) {
		super();
		this.routeName = routeName;
		this.workplanTrips = workplanTrips;
	}
	

	public RouteWorkplanTrip() {
		super();
	}

	public RouteWorkplanTrip(String routeName, String groupName, List<DriverTrip> workplanTrips) {
		super();
		this.routeName = routeName;
		this.groupName = groupName;
		this.workplanTrips = workplanTrips;
	}

}
