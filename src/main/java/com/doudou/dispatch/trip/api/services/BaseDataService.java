package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.entities.Driver;
import com.doudou.dispatch.trip.api.entities.TVehicle;

import java.util.List;

public interface BaseDataService {

    public Driver getDriverById(String driverId,String company);

    public TVehicle getVehicleById(String vehicleId,String company);

    public List<String> getRouteMsgs(String company);

    public List<TVehicle> getVehicles(String routeName, String company);

    public List<Driver> getDrivers(String routeName, String company);
}
