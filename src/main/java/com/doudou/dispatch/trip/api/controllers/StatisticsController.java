package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.bean.QueryResult;
import com.doudou.dispatch.trip.api.services.TripStatistsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class StatisticsController {

    @Autowired
    private TripStatistsService tripStatistsService;

    @RequestMapping("/driver/trip")
    public JsonResult getDriverCount(String workdate,String routeName,String statisticsType) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(routeName)) {
            return jsonResult.setDefine("请选择线路");
        }
        if(StringUtils.isEmpty(workdate)) {
            return jsonResult.setDefine("请选择月份");
        }
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(tripStatistsService.getDriverTrip(workdate, routeName, statisticsType));
    }

    @RequestMapping("/vehicle/trip")
    public JsonResult getVehicleTripDetail(String workdate,String routeName) throws IllegalAccessException {
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(routeName)) {
            return jsonResult.setDefine("请选择线路");
        }
        if(StringUtils.isEmpty(workdate)) {
            return jsonResult.setDefine("请选择月份");
        }
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(tripStatistsService.getVehicleTripAndMileage(workdate, routeName,1,1000,"1"));
    }


    @RequestMapping("/driverVehicle/trip")
    public JsonResult getDriverTripDetail(String workdate,String routeName){
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(workdate) || StringUtils.isEmpty(routeName)){
            return jsonResult.setDefine("参数为空");
        }
        QueryResult qu = tripStatistsService.getTripDeatail(workdate, routeName, "1", 1, 1000);
        if(null != qu){
            return jsonResult.setCode(HttpApiConstans.success)
                             .setDefine(HttpApiConstans.succe_define)
                             .setResult(qu);
        }
        return jsonResult;
    }

}
