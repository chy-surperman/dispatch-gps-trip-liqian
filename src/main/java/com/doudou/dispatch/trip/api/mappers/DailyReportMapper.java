package com.doudou.dispatch.trip.api.mappers;

import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.BigDailyWorkplan;
import com.doudou.dispatch.trip.api.entities.DriverTrip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyReportMapper {
	public List<BigDailyWorkplan> selectBigDailyWorkplanReport(@Param("routeName") String routeName,
															   @Param("date") String date, @Param("isThroughMonitorSite") String ThroughMonitorSite);
	
	public List<DriverTrip> selectDriverTrips(@Param("routeName") String routeName,
											  @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("statisticsType") String statisticsType);
	
	public List<DriverTrip> selectTotalVehicleTripAndMileage(@Param("routeName") String routeName,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("statisticsType") String statisticsType);
	
	public int selectVehicleTripAndMileageCount(@Param("routeName") String routeName,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("statisticsType") String statisticsType);
	
	public List<DriverTrip> selectTripDay(@Param("routeName") String routeName,
			@Param("startDate") String startDate,@Param("endDate") String endDate);
	
	public List<DriverTrip> selectVehicleTripAndMileage(
			@Param("routeName") String routeName,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate,
			@Param("start") int start,
			@Param("pageSize") int pageSize,
			@Param("statisticsType") String statisticsType);
	
	public List<DriverTrip> selectTripDetail(
			@Param("routeName") String routeName,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate,
			@Param("start") int start,
			@Param("statisticsType") String statisticsType,
			@Param("pageSize") int pageSize);
	
	public int selectTripDetailCount(@Param("routeName") String routeName,
								@Param("startDate") String startDate,
								@Param("statisticsType") String statisticsType,
								@Param("endDate") String endDate);

	/**
	 * 统计司机趟次
	 * @param driverId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Workplan> selectDriverWorkplanStatics(@Param("driverId") String driverId,
													  @Param("startDate") String startDate,
													  @Param("endDate") String endDate);
	
	public List<Workplan> selectRouteWorkplanStatics(@Param("routeName") String routeName,@Param("date") String date);
}
