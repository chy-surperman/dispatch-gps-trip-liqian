package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class VehicleMileage {
    private String workdate;
    private Double mileageTotal;
    private Double operationMileage;

    private String selfNum;
    private String plateNum;
    private String vehicleId;
}
