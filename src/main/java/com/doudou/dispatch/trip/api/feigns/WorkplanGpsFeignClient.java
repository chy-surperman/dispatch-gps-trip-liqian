package com.doudou.dispatch.trip.api.feigns;

import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.entities.WorkplanPostGps;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Date;

public interface WorkplanGpsFeignClient {

    @RequestLine("POST /workplan/createGps")
    @Headers({
            "Content-Type: application/json;charset=UTF-8",
            "AccessSource: wechat"
    })
    public JsonResult saveWorkplanGps(WorkplanPostGps workplan);

    @RequestLine("GET /workplan/confirm/gps?date={date}&sign={sign}")
    @Headers({
            "AccessSource: wechat"
    })
    public JsonResult comfireWorkplanGps(@Param("date") String date, @Param("sign") String sign);

    @RequestLine("GET /workplan/cancel/gps?date={date}&sign={sign}")
    @Headers({
            "AccessSource: wechat"
    })
    public JsonResult cancelWorkplanGps(@Param("date") String date, @Param("sign") String sign);

    @RequestLine("GET /workplan/moveSlaveGps?startDate={startDate}&endDate={endDate}&vehicleId={vehicleId}&sign={sign}")
    @Headers({
            "AccessSource: wechat"
    })
    public JsonResult moveSlaveGps(@Param("startDate") String startDate,
                                   @Param("endDate") String endDate,
                                   @Param("vehicleId") String vehicleId,
                                   @Param("sign") String sign);

    @RequestLine("GET /workplan/deleteGps?startTime={startTime}&endTime={endTime}&vehicleId={vehicleId}&gpsTableName={gpsTableName}")
    @Headers({
            "AccessSource: wechat"
    })
    public JsonResult deleteGps(@Param("startTime") String startTime,
                                @Param("endTime") String endTime,
                                @Param("vehicleId") String vehicleId,
                                @Param("gpsTableName") String gpsTableName );


    @RequestLine("GET /gps/calMileage?workdate={workdate}&vehicleId={vehicleId}")
    @Headers({
            "AccessSource: wechat"
    })
    public JsonResult calcVehicleMeleageMethon(@Param("workdate") String workdate,
                                @Param("vehicleId") String vehicleId
                                );
}
