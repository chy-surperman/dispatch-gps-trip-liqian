package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.dispatch.gps.commons.utils.GPSUtil;
import com.doudou.dispatch.trip.api.entities.GpsCoordHistroy;
import com.doudou.dispatch.trip.api.feigns.GpsFeignClient;
import com.doudou.dispatch.trip.api.services.PostGpsService;
import com.doudou.dispatch.trip.api.services.QueryGpsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/location")
public class GpsController {

    @Autowired
    private GpsFeignClient gpsFeignClient;

    @Autowired
    private PostGpsService postGpsService;

    @Autowired
    private QueryGpsService queryGpsService;

    @RequestMapping("/locus")
    public JsonResult getVehicleLocusMapGps(String routeName,String startTime, String endTime, String vehicleId) {
//        JsonResult jsonResult = new JsonResult();
//        if(StringUtils.isEmpty(startTime)) {
//            return jsonResult.setDefine("请输入起始时间");
//        }
//        if(StringUtils.isEmpty(endTime)) {
//            return jsonResult.setDefine("请输入截止时间");
//        }
//
//        if(!startTime.split(" ")[0].equals(endTime.split(" ")[0])) {
//            return jsonResult.setDefine("轨迹查询不能跨天查询");
//        }
//
//        if(StringUtils.isEmpty(vehicleId)) {
//            return jsonResult.setDefine("请选择车辆");
//        }
//
//        String startDay = startTime.split(" ")[0];
//        Date startDate = DateUtil.getDateByString(startDay, DateStyle.YYYY_MM_DD);
//        if(startDate.compareTo(DateUtil.getDateByString("2019-12-31",DateStyle.YYYY_MM_DD)) <= 0){
//            List<GenerateGps> generateGpsList = queryGpsService.queryGps(routeName, startTime, endTime, vehicleId, startDate);
//            if(null == generateGpsList || generateGpsList.size() == 0){
//                return jsonResult.setResult("该时间段无轨迹");
//            }
//
//            for (int i = 0; i < generateGpsList.size(); i++) {
//                GenerateGps generateGps = generateGpsList.get(i);
//                double[] doubles = GPSUtil.gps84_To_Gcj02(generateGps.getLatitude(), generateGps.getLongitude());
//                generateGps.setLatitude(new Double(doubles[0]).floatValue());
//                generateGps.setLongitude(new Double(doubles[1]).floatValue());
//            }
//            return jsonResult.setCode(HttpApiConstans.success)
//                    .setResult(generateGpsList)
//                    .setDefine(HttpApiConstans.succe_define);
//        }
//
//        Date start = DateUtil.getDateByString(startTime, DateStyle.YYYY_MM_DD_HH_MM_SS);
//        Date end = DateUtil.getDateByString(endTime,DateStyle.YYYY_MM_DD_HH_MM_SS);
//        List<GpsLocus> vehicleLocus = postGpsService.getVehicleLocus(start, end, vehicleId);
//        return jsonResult.setCode(HttpApiConstans.success)
//                         .setResult(vehicleLocus)
//                         .setDefine(HttpApiConstans.succe_define);
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(startTime))
            return jsonResult.setDefine("");
        if (StringUtils.isEmpty(endTime))
            return jsonResult.setDefine("");
        if (!startTime.split(" ")[0].equals(endTime.split(" ")[0]))
            return jsonResult.setDefine("");
        if (StringUtils.isEmpty(vehicleId))
            return jsonResult.setDefine("");
        return this.gpsFeignClient.getVehicleLocusMapGps(startTime, endTime, vehicleId, 1, "gpsDB");
    }

    @RequestMapping("/connection")
    public JsonResult sqlserver(String l,String u,String p){
        JsonResult jsonResult = new JsonResult();
        log.info("connection info:{},{},{}",l,u,p);
        return jsonResult;
    }

    @RequestMapping("/reports")
    public JsonResult reportGps(@RequestBody List<GpsCoordHistroy> gpsList){
        JsonResult jsonResult = new JsonResult();
        if(null != gpsList){
            gpsList.stream().forEach(System.out::println);
        }
        return jsonResult;
    }
}
