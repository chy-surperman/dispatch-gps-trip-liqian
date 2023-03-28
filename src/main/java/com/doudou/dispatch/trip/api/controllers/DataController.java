package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.doudou.dispatch.trip.api.entities.BigDailyWorkplanReport;
import com.doudou.dispatch.trip.api.entities.TUser;
import com.doudou.dispatch.trip.api.feigns.WorkplanFeignClient;
import com.doudou.dispatch.trip.api.services.BaseDataService;
import com.doudou.dispatch.trip.api.services.TUserService;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private BaseDataService baseDataService;

    @Autowired
    private WorkplanFeignClient workplanFeignClient;

    @Autowired
    private TUserService tUserService;

    @Qualifier("dbWorkplanService")
    @Autowired
    private WorkplanService workplanService;

    @RequestMapping("/routes")
    public JsonResult getRouteNames(@RequestHeader("Authorization") String userName) {
        JsonResult jsonResult = new JsonResult();
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if (null == sessionTUser) {
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(baseDataService.getRouteMsgs(sessionTUser.getCompany()));
    }

    @RequestMapping("/vehicles")
    public JsonResult getVehicles(@RequestHeader("Authorization") String userName, String routeName) {
        JsonResult jsonResult = new JsonResult();
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if (null == sessionTUser) {
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(baseDataService.getVehicles(routeName, sessionTUser.getCompany()));
    }

    @RequestMapping("/dynamicVehicles")
    public JsonResult getDynamicVehicles(@RequestHeader("Authorization") String userName, String routeName,String startTime, String endTime) {
        JsonResult jsonResult = new JsonResult();
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if (null == sessionTUser) {
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        if(StringUtils.isEmpty(startTime)) {
            return jsonResult.setDefine("请输入起始时间");
        }
        if(StringUtils.isEmpty(endTime)) {
            return jsonResult.setDefine("请输入截止时间");
        }

        if(!startTime.split(" ")[0].equals(endTime.split(" ")[0])) {
            return jsonResult.setDefine("轨迹查询不能跨天查询");
        }
        String date = startTime.split(" ")[0];
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(workplanService.queryWorkplanVehicles(date,routeName));
    }

    @RequestMapping("/drivers")
    public JsonResult getDrivers(@RequestHeader("Authorization") String userName, String routeName) {
        JsonResult jsonResult = new JsonResult();
        TUser sessionTUser = tUserService.getSessionTUser(userName);
        if (null == sessionTUser) {
            return jsonResult.setCode(HttpApiConstans.not_login);
        }
        return jsonResult.setCode(HttpApiConstans.success)
                .setResult(baseDataService.getDrivers(routeName, sessionTUser.getCompany()));
    }

    @RequestMapping("/daily/big")
    public JsonResult getWorkplanBigDaily(String routeName,String startDate){
//        JsonResult jsonResult = new JsonResult();
//        if(StringUtils.isEmpty(routeName)){
//            return jsonResult.setDefine("线路为空");
//        }
//        if(StringUtils.isEmpty(startDate)){
//            return jsonResult.setDefine("查询日期为空");
//        }
        return workplanFeignClient.getBigDailyWorkplan(routeName, startDate);
    //        List<BigDailyWorkplanReport> bigDailyWorkplanReport = workplanService.getBigDailyWorkplanReport(routeName, startDate, "1");
//        Map<String, Object> res = new HashMap<>();
//        res.put("company", "通畅巴士");
//        res.put("datas", bigDailyWorkplan);
//        return jsonResult.setCode(HttpApiConstans.success)
//                         .setDefine(HttpApiConstans.succe_define)
//                         .setResult(res);
    }

}
