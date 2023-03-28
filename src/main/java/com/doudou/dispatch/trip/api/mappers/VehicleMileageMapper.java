package com.doudou.dispatch.trip.api.mappers;

import com.doudou.dispatch.trip.api.entities.VehicleMileage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleMileageMapper {

    public List<VehicleMileage> selectVehicleMileage(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("routeName") String routeName);

}
