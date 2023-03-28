package com.doudou.dispatch.trip.api.mappers;

import com.dispatch.gps.commons.entities.Driver;
import com.doudou.dispatch.trip.api.entities.TVehicle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDataMapper {

    public List<String> selectRouteNames(String company);

    public List<Driver> selectCsCityDriver();

    public List<String> selectRepeatDriver();

    public List<TVehicle> selectCsCityVehicle();

    public List<Driver> selectDriverByName(@Param("driverName") String driverName);

    public int deleteById(@Param("id") int id);

    public int updateWorkplan(@Param("driverId") String driverId,@Param("newDriverId") String newDriverId);
}
