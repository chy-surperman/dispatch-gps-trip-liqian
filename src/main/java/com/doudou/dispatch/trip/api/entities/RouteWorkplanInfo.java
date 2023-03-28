package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class RouteWorkplanInfo {

    private String type;
    private String routeName;
    private int gpsSize;
    private int runTime;
}
