package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class QueryWorkplan {
    private long id;
    private String date;
    private String routeName;
    private String vehicleId;
    private String driverId;
}
