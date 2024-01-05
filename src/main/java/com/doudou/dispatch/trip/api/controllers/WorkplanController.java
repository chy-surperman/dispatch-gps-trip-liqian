package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.entities.Driver;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.*;
import com.doudou.dispatch.trip.api.feigns.GpsFeignClient;
import com.doudou.dispatch.trip.api.services.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workplan")
public class WorkplanController {

    @Qualifier("dbWorkplanService")
    @Autowired
    private WorkplanService workplanService;

    @Autowired
    private GpsFeignClient gpsFeignClient;

    @Autowired
    private GpsGenerateService gpsGenerateService;

    @Autowired
    private WorkplanGpsService workplanGpsService;

    @Autowired
    private TUserService tUserService;

    @Autowired
    private BaseDataService baseDataService;

    @RequestMapping("/runtime")
    public JsonResult getRouteRunTime(String routeName){
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(routeName)){
            return jsonResult.setDefine("请输入线路");
        }
        Map<String,Integer> resultMap = new HashMap<>(2);
        resultMap.put("up",this.gpsGenerateService.getRouteRunTime(routeName,"0"));
        resultMap.put("down",this.gpsGenerateService.getRouteRunTime(routeName,"1"));
        return jsonResult.setCode(HttpApiConstans.success)
                         .setResult(resultMap);
    }

    @RequestMapping("/query")
    public JsonResult queryWorkplans(QueryWorkplan queryWorkplan) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(queryWorkplan.getDate())) {
            return jsonResult.setDefine("请输入查询日期");
        }
        if (StringUtils.isEmpty(queryWorkplan.getRouteName())) {
            return jsonResult.setDefine("请输入线路");
        }

        return jsonResult.setDefine(HttpApiConstans.succe_define)
                .setCode(HttpApiConstans.success)
                .setResult(workplanService.queryWorkplans(queryWorkplan));
    }

    @RequestMapping("/gps")
    public JsonResult queryWorkplanGps(String startDate, String endDate, String vehicleId, String routeName) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(startDate)) {
            return jsonResult.setDefine("请输入起始时间");
        }
        if (StringUtils.isEmpty(endDate)) {
            return jsonResult.setDefine("请输入截止时间");
        }

        if (!startDate.split(" ")[0].equals(endDate.split(" ")[0])) {
            return jsonResult.setDefine("轨迹查询不能跨天查询");
        }

        if (StringUtils.isEmpty(vehicleId)) {
            return jsonResult.setDefine("请选择车辆");
        }

       List<GpsLocus> gpsLocusList = workplanService.getWorkplanGps(startDate, endDate, vehicleId, routeName);
//        List<GpsLocus> gpsLocusList = new ArrayList<>();
       if(null == gpsLocusList || gpsLocusList.size() == 0){
            return jsonResult.setCode(7)
                             .setDefine("无GPS");
        }
        return jsonResult.setCode(HttpApiConstans.success)
                         .setDefine(HttpApiConstans.succe_define)
                         .setResult(gpsLocusList);

    }

    @RequestMapping("/generterGps")
    public JsonResult generterWorkplanGps(String startDate, String endDate, String vehicleId, String routeName, String starttag) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(startDate)) {
            return jsonResult.setDefine("请输入起始时间");
        }
        if (StringUtils.isEmpty(endDate)) {
            return jsonResult.setDefine("请输入截止时间");
        }

        if (!startDate.split(" ")[0].equals(endDate.split(" ")[0])) {
            return jsonResult.setDefine("轨迹查询不能跨天查询");
        }

        if (StringUtils.isEmpty(vehicleId)) {
            return jsonResult.setDefine("请选择车辆");
        }

        List<GenerateGps>  generateGpss =gpsGenerateService.generateGps(startDate, endDate, vehicleId, routeName, starttag);
        if (null != generateGpss) {
            Map<String,Object> resMap = new HashMap<>(2);
            resMap.put("sign","");
            resMap.put("datas",generateGpss);
            return jsonResult.setCode(HttpApiConstans.success)
                             .setResult(resMap)
                             .setDefine(HttpApiConstans.succe_define);
        }
        return jsonResult;
    }

    @RequestMapping("/cancelGps")
    public JsonResult cancelWorkplanGps(String date,String sign){
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(date)) {
            return jsonResult.setDefine("请输入起始时间");
        }
        if (StringUtils.isEmpty(sign)) {
            return jsonResult.setDefine("请输入截止时间");
        }
        if(workplanGpsService.cancelWorkplanGps(DateUtil.getDateByString(date,DateStyle.YYYY_MM_DD),sign)){
            return jsonResult.setCode(HttpApiConstans.success);
        }
        return jsonResult;
    }

    @RequestMapping("/delete")
    public JsonResult deleteWorkplan(long id){
        JsonResult jsonResult = new JsonResult();
        if(id == 0){
            return jsonResult.setDefine("请输入Id");
        }
        try {
            if(this.workplanService.deleteWorkplan(id)){
                return jsonResult.setCode(HttpApiConstans.success);
            }
        } catch (Exception e) {
            return jsonResult.setDefine(e.getMessage());
        }
        return jsonResult;
    }


    @RequestMapping("/deleteWithoutGps")
    public JsonResult deleteWorkplanWithoutGps(long id){
        JsonResult jsonResult = new JsonResult();
        if(id == 0){
            return jsonResult.setDefine("请输入Id");
        }
        try {
            if(this.workplanService.deleteWorkplanWithoutGps(id)){
                return jsonResult.setCode(HttpApiConstans.success);
            }
        } catch (Exception e) {
            return jsonResult.setDefine(e.getMessage());
        }
        return jsonResult;
    }

    @RequestMapping("/submitWorkplan")
    public JsonResult submitWorkplan(@RequestHeader("Authorization") String userName,
                                     @RequestBody WorkplanChangeRecode changeRecord) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(userName)){
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if(null == sessionTUser){
            return jsonResult.setCode(HttpApiConstans.not_login);
        }

        CreateWorkplan workplan = changeRecord.getAssociationWorkplan();

        if(StringUtils.isEmpty(workplan.getDriverId())) {
            return jsonResult.setDefine("请选择司机");
        }
        if(StringUtils.isEmpty(workplan.getVehicleId())) {
            return jsonResult.setDefine("请选择车辆");
        }
        if(StringUtils.isEmpty(workplan.getStarttag())) {
            return jsonResult.setDefine("请选择趟次发班方向");
        }
        if(StringUtils.isEmpty(workplan.getRouteName())) {
            return jsonResult.setDefine("请选择线路");
        }
        // 判断时间
        if(StringUtils.isEmpty(workplan.getDateString())) {
            return jsonResult.setDefine("请选择趟次日期");
        }

        if(StringUtils.isEmpty(workplan.getDepartureString())) {
            return jsonResult.setDefine("请输入发班时间");
        }

        if(StringUtils.isEmpty(workplan.getArrivalString())) {
            return jsonResult.setDefine("请输入到达时间");
        }

        try {
            workplan.setDate(DateUtil.getDate(workplan.getDateString(), DateStyle.YYYY_MM_DD));
            workplan.setScheduleTime(DateUtil.getDate(workplan.getDepartureString(), DateStyle.HH_MM_SS));
            workplan.setArrivalTime(DateUtil.getDate(workplan.getArrivalString(), DateStyle.HH_MM_SS));
            workplan.setDepartureTime(workplan.getScheduleTime());
            workplan.setIsThroughUpMonitorSite(1);
            workplan.setDispatchTime(workplan.getScheduleTime());
        } catch (ParseException e1) {
            e1.printStackTrace();
            return jsonResult.setDefine("时间格式错误");
        }

        Driver driver = baseDataService.getDriverById(workplan.getDriverId(),sessionTUser.getCompany());
        if (null == driver) {
            return jsonResult.setDefine("不存在的司机");
        }

        TVehicle vehicle = baseDataService.getVehicleById(workplan.getVehicleId(),sessionTUser.getCompany());
        if (null == vehicle) {
            return jsonResult.setDefine("不存在的车辆");
        }

        workplan.setDriverName(driver.getDriverName());
        workplan.setPlateNum(vehicle.getPlateNum());
        workplan.setSelfNum(vehicle.getSelfNum());

        try {
            int result = workplanService.saveManulWorkplanChangeRecode(changeRecord);
            if(result == 1) {
                return jsonResult.setCode(HttpApiConstans.success)
                        .setDefine(HttpApiConstans.succe_define);
            }
            if(result < 0) {
                return jsonResult.setDefine("添加趟次失败");
            }
            if(result == -1) {
                return jsonResult.setDefine("不能将原有的趟次修改为另一个司机的趟次");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }


    @RequestMapping("/saveSubmitWorkplan")
    public JsonResult saveSubmitWorkplan(@RequestHeader("Authorization") String userName,
                                     @RequestBody WorkplanChangeRecode changeRecord) {
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(userName)){
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if(null == sessionTUser){
            return jsonResult.setCode(HttpApiConstans.not_login);
        }

        CreateWorkplan workplan = changeRecord.getAssociationWorkplan();

        if(StringUtils.isEmpty(workplan.getDriverId())) {
            return jsonResult.setDefine("请选择司机");
        }
        if(StringUtils.isEmpty(workplan.getVehicleId())) {
            return jsonResult.setDefine("请选择车辆");
        }
        if(StringUtils.isEmpty(workplan.getStarttag())) {
            return jsonResult.setDefine("请选择趟次发班方向");
        }
        if(StringUtils.isEmpty(workplan.getRouteName())) {
            return jsonResult.setDefine("请选择线路");
        }
        // 判断时间
        if(StringUtils.isEmpty(workplan.getDateString())) {
            return jsonResult.setDefine("请选择趟次日期");
        }

        if(StringUtils.isEmpty(workplan.getDepartureString())) {
            return jsonResult.setDefine("请输入发班时间");
        }

        if(StringUtils.isEmpty(workplan.getArrivalString())) {
            return jsonResult.setDefine("请输入到达时间");
        }

        try {
            workplan.setDate(DateUtil.getDate(workplan.getDateString(), DateStyle.YYYY_MM_DD));
            workplan.setScheduleTime(DateUtil.getDate(workplan.getDepartureString(), DateStyle.HH_MM_SS));
            workplan.setArrivalTime(DateUtil.getDate(workplan.getArrivalString(), DateStyle.HH_MM_SS));
            workplan.setDepartureTime(workplan.getScheduleTime());
            workplan.setIsThroughUpMonitorSite(1);
            workplan.setDispatchTime(workplan.getScheduleTime());
            workplan.setTripValue(Float.valueOf("0.5"));
        } catch (ParseException e1) {
            e1.printStackTrace();
            return jsonResult.setDefine("时间格式错误");
        }

        Driver driver = baseDataService.getDriverById(workplan.getDriverId(),sessionTUser.getCompany());
        if (null == driver) {
            return jsonResult.setDefine("不存在的司机");
        }

        TVehicle vehicle = baseDataService.getVehicleById(workplan.getVehicleId(),sessionTUser.getCompany());
        if (null == vehicle) {
            return jsonResult.setDefine("不存在的车辆");
        }

        workplan.setDriverName(driver.getDriverName());
        workplan.setPlateNum(vehicle.getPlateNum());
        workplan.setSelfNum(vehicle.getSelfNum());

        try {
            int result = workplanService.saveManulWorkplanChangeRecodeWithoutGPS(changeRecord);
            if(result == 1) {
                return jsonResult.setCode(HttpApiConstans.success)
                        .setDefine(HttpApiConstans.succe_define);
            }
            if(result < 0) {
                return jsonResult.setDefine("添加趟次失败");
            }
            if(result == -1) {
                return jsonResult.setDefine("不能将原有的趟次修改为另一个司机的趟次");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

}
