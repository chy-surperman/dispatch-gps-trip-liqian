package com.doudou.dispatch.trip.api.mappers;

import com.doudou.dispatch.trip.api.entities.GpsTemplate;
import com.doudou.dispatch.trip.api.entities.Gpsreport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GpsreportMapper {

    public List<GpsTemplate> selectByRouteNameAndStarttag(@Param("routeName") String routeName,
                                                      @Param("starttag") String starttag);

    public List<Gpsreport> selectTime(@Param("gpsTableName") String gpsTableName, @Param("departureTime") Date departureTime, @Param("arrivalTime") Date arrivalTime, @Param("vehicleId") String vehicleId);

    public int deleteGps(@Param("gpsTableName") String gpsTableName, @Param("departureTime") Date departureTime, @Param("arrivalTime") Date arrivalTime, @Param("vehicleId") String vehicleId);
}
