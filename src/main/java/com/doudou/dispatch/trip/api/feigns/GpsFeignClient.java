package com.doudou.dispatch.trip.api.feigns;

import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.doudou.dispatch.trip.commons.HttpRespResult;
import feign.Param;
import feign.RequestLine;

import java.util.Date;
import java.util.List;

public interface GpsFeignClient {

    @RequestLine("GET /gps/locus?startDate={startDate}&endDate={endDate}&vehicleId={vehicleId}&filterDrift={filterDrift}&dbType={dbType}")
    public JsonResult getVehicleLocusMapGps(@Param("startDate") String startDate,
                                            @Param("endDate") String endDate,
                                            @Param("vehicleId") String vehicleId,
                                            @Param("filterDrift") int filterDrift,
                                            @Param("dbType") String dbType);

    @RequestLine("GET /gps/locus?startDate={startDate}&endDate={endDate}&vehicleId={vehicleId}&filterDrift={filterDrift}&dbType={dbType}")
    public HttpRespResult<List<GpsLocus>> getVehicleGps(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("vehicleId") String vehicleId,
                                                        @Param("filterDrift") int filterDrift,
                                                        @Param("dbType") String dbType);



}
