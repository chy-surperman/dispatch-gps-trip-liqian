package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.bean.QueryResult;
import com.doudou.dispatch.trip.api.entities.DriverTrip;

import java.util.List;

public interface TripStatistsService {

    public QueryResult getTripDeatail(String workdate, String routeName, String statisticsType, int page, int pageSize);

    public QueryResult getVehicleTripAndMileage(String workdate,String routeName,int page,int pageSize,String statisticsType) throws IllegalAccessException;

    public List<DriverTrip> getDriverTrip(String workdate, String routeName, String statisticsType);
}
