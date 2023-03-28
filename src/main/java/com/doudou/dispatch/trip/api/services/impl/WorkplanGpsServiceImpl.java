package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.WorkplanPostGps;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;

import com.doudou.dispatch.trip.api.feigns.WorkplanGpsFeignClient;
import com.doudou.dispatch.trip.api.services.WorkplanGpsService;
import com.doudou.dispatch.trip.commons.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("workplanGpsService")
public class WorkplanGpsServiceImpl implements WorkplanGpsService {

    @Autowired
    private WorkplanGpsFeignClient workplanGpsFeignClient;

    @Autowired
    private IdWorker idWorker;

    @Override
    public String saveWorkplanGps(Date date, List<GenerateGps> generateGps) {
        WorkplanPostGps workplanPostGps = new WorkplanPostGps();
        workplanPostGps.setDate(DateUtil.getDateByDs(date, DateStyle.YYYY_MM_DD));
        workplanPostGps.setSign(idWorker.nextId() + "");

        for(GenerateGps gps:generateGps){
            gps.setCreateTime(null);
        }
        workplanPostGps.setGpsLists(generateGps);

        if(workplanGpsFeignClient.saveWorkplanGps(workplanPostGps).getCode() == HttpApiConstans.success){
            return workplanPostGps.getSign();
        }
        return null;
    }

    @Override
    public boolean confireWorkplanGps(Date date, String sign) {
        return workplanGpsFeignClient.comfireWorkplanGps(DateUtil.getDateByDs(date, DateStyle.YYYY_MM_DD), sign).getCode() == HttpApiConstans.success;
    }

    @Override
    public boolean cancelWorkplanGps(Date date, String sign) {
        if(StringUtils.isEmpty(sign)){
            return true;
        }
        return workplanGpsFeignClient.cancelWorkplanGps(DateUtil.getDateByDs(date, DateStyle.YYYY_MM_DD), sign).getCode() == HttpApiConstans.success;
    }

    @Override
    public String moveSlaveGps(String startDate, String endDate, String vehicleId) {
        String sign = idWorker.nextId() + "";
        if (workplanGpsFeignClient.moveSlaveGps(startDate, endDate, vehicleId, sign).getCode() == HttpApiConstans.success) {
            return sign;
        }
        return null;
    }
}
