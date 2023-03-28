package com.doudou.dispatch.trip.api.mappers;

import com.dispatch.gps.commons.entities.WorkplanPostGps;
import org.apache.ibatis.annotations.Param;


public interface PostGpsMapper {
	
	public int savePostGps(@Param("workplanPostGps") WorkplanPostGps workplanPostGps, @Param("gpsTableName") String gpsTableName);
	
	public int comfirePostGps(@Param("sign") String sign,@Param("gpsTableName") String tableName);
	
	public int cancelPostGps(@Param("sign") String sign,@Param("gpsTableName") String tableName);

	public int moveSlaveGpsInsert(@Param("gpsTableName") String gpsTableName,
							@Param("departureTime")String departureTime, 
							@Param("arrivalTime")String arrivalTime, 
							@Param("vehicleId")String vehicleId,
							@Param("sign") String sign);
}
