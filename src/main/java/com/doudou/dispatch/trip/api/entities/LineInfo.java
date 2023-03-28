package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class LineInfo {
    private String routeName;
    private String startTime;
    private String endTime;

    private int driverTrip;

    private int[] runTime;
    private int gpsFreq;
    private double routeDistance;
    private double gpsDistance;
}
