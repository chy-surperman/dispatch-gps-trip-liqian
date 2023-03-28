package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class TVehicle {
    private int id;
    private String vehicleId;
    private String plateNum;
    private String routeName;
    private String selfNum;
}
