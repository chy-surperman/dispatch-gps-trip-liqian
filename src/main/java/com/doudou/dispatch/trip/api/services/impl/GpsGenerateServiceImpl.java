package com.doudou.dispatch.trip.api.services.impl;

import com.alibaba.fastjson.JSON;
import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.GpsTemplate;
import com.doudou.dispatch.trip.api.mappers.GpsreportMapper;
import com.doudou.dispatch.trip.api.services.GpsGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("gpsGenerateService")
public class GpsGenerateServiceImpl implements GpsGenerateService {

    private Map<String,List<GpsTemplate>> gpsTemplateMap = new ConcurrentHashMap<>(64);

    private Map<String,Integer> routeTimeMap = new ConcurrentHashMap<>(32);

    @Autowired
    private GpsreportMapper gpsreportMapper;

    private Random random = new Random();

    @Override
    public List<GenerateGps> generateGps(String startDate, String endDate, String vehicleId, String routeName, String starttag) {
        //计算运行时长
        Date arrival = DateUtil.getDateByString(endDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date departure = DateUtil.getDateByString(startDate,DateStyle.YYYY_MM_DD_HH_MM_SS);

        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长

        List<GpsTemplate> gpsTemplates = this.getGpsTemplate(routeName,starttag);
        if(null == gpsTemplates){
            return null;
        }

        GenerateGps firstGps = this.createPoint(gpsTemplates.get(0),vehicleId);
        firstGps.setCreateTime(DateUtil.getDateByString(startDate,DateStyle.YYYY_MM_DD_HH_MM_SS));
        firstGps.setTime(DateUtil.getDateByDs(firstGps.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));

        List<GenerateGps> generateGpsList = new ArrayList<>(gpsTemplates.size());
        generateGpsList.add(firstGps);
        int gpsSize = runTime / 10;
        boolean isAddGps = gpsTemplates.size() <= gpsSize ? true : false;
        if(isAddGps){
            return this.generateAddGps(generateGpsList,gpsTemplates,vehicleId,departure,arrival);
        }else {
            return this.generateSubGps(generateGpsList,gpsTemplates,vehicleId,departure,arrival);
        }
    }

    @Override
    public int getRouteRunTime(String routeName, String starttag) {
        List<GpsTemplate> gpsTemplate = this.getGpsTemplate(routeName, starttag);
        if(null == gpsTemplate || gpsTemplate.size() == 0){
            return 0;
        }
        return gpsTemplate.size() * 10 / 60;
    }

    private List<GenerateGps> generateSubGps(List<GenerateGps> generateGpsList,
                                             List<GpsTemplate> gpsTemplates,
                                             String vehicleId,
                                             Date departure,
                                             Date arrival){
        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长
        int gpsSize = runTime / 10;
        int subCount = gpsTemplates.size() - gpsSize;

        int subIndex = 0;
        for (int i = 1; i < gpsTemplates.size(); i++) {
            if(gpsTemplates.get(i).getSpeed() <= 0 && subCount > 0){
                if(i - subIndex > 1){
                    subIndex = i;
                    subCount--;
                    continue;
                }
            }else{
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId);
                generateGpsList.add(point);
            }
        }

        return generateGpsList;
    }

    private List<GenerateGps> generateAddGps(List<GenerateGps> generateGpsList,
                                             List<GpsTemplate> gpsTemplates,
                                             String vehicleId,
                                             Date departure,
                                             Date arrival){
        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长
        int gpsSize = runTime / 10;
        int addCount = gpsSize - gpsTemplates.size();
        int spped0Count = 0;
        for (int i = 0; i < gpsTemplates.size(); i++){
            if(gpsTemplates.get(i).getSpeed() <= 0 ){
                spped0Count++;
            }
        }
        int repeat = addCount < spped0Count ? 1 : addCount / spped0Count;
        for (int i = 1; i < gpsTemplates.size(); i++) {
            if(gpsTemplates.get(i).getSpeed() <= 0 && addCount > 0){
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId);
                generateGpsList.add(point);
                //速度为0点重复添加
                for (int j = 0; j < repeat && addCount > 0; j++) {
                    point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId);
                    generateGpsList.add(point);
                    addCount--;
                }
            }else{
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId);
                generateGpsList.add(point);
            }
        }

        return generateGpsList;
    }

    private GenerateGps createPoint(GpsTemplate gpsTemplate,GenerateGps preGps,String vehicleId){
        GenerateGps gps = this.createPoint(gpsTemplate, vehicleId);

        int interval = 10;
        int randomValue = random.nextInt(3);
        if (randomValue == 0) {
            gps.setCreateTime(new Date(preGps.getCreateTime().getTime() + interval * 1000));
        }else if(randomValue == 1){
            gps.setCreateTime(new Date(preGps.getCreateTime().getTime() + (interval - 1) * 1000));
        }else {
            gps.setCreateTime(new Date(preGps.getCreateTime().getTime() + (interval + 1) * 1000));
        }

        gps.setTime(DateUtil.getDateByDs(gps.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
        return gps;
    }

    private GenerateGps createPoint(GpsTemplate gpsTemplate,String vehicleId){
        GenerateGps generateGps = new GenerateGps();
        generateGps.setVehicleId(vehicleId);

        int randomValue = random.nextInt(2);
        if(randomValue == 0){
            int prefix = new Float(gpsTemplate.getLatitude() * 10000).intValue();
            generateGps.setLatitude(prefix / 10000f + random.nextInt(100) / 1000000f);
            generateGps.setLongitude(gpsTemplate.getLongitude());
        }else{
            int prefix = new Float(gpsTemplate.getLongitude() * 10000).intValue();
            generateGps.setLongitude(prefix / 10000f + random.nextInt(100) / 1000000f);
            generateGps.setLatitude(gpsTemplate.getLatitude());
        }

        float speed = gpsTemplate.getSpeed();
        if(gpsTemplate.getSpeed() >= 6 && gpsTemplate.getSpeed() < 35){
            speed = randomValue == 0 ? gpsTemplate.getSpeed() - random.nextInt(3) : gpsTemplate.getSpeed() + random.nextInt(3);
        }else if(gpsTemplate.getSpeed() >= 35){
            speed = randomValue == 0 ? gpsTemplate.getSpeed() - random.nextInt(4) : gpsTemplate.getSpeed() + random.nextInt(4);
        }
        generateGps.setSpeed(speed);
        return generateGps;
    }

    public List<GpsTemplate> getGpsTemplate(String routeName,String starttag){
        //Map key
        String key = routeName + "_" + starttag;

        //判断是否存在线路GPS的缓存
        List<GpsTemplate> gpsTemplates = gpsTemplateMap.get(key);
        if(null == gpsTemplates){
            //不存在，查询数据库查出缓存
            gpsTemplates = gpsreportMapper.selectByRouteNameAndStarttag(routeName,starttag);
            if(null != gpsTemplates && gpsTemplates.size() > 0){
                gpsTemplateMap.put(key,gpsTemplates);
            }
        }
        return JSON.parseArray(JSON.toJSONString(gpsTemplates),GpsTemplate.class);
    }

    private int timeDiff(Date start,Date end){
        return new Long((end.getTime() - start.getTime()) / 1000 / 60).intValue();
    }

    private int timeDiffSecond(Date start,Date end){
        return new Long((end.getTime() - start.getTime()) / 1000).intValue();
    }

    private Date getWorkplanDepartue(Workplan workplan){
        return DateUtil.getDateByString(workplan.getDate() + " " + workplan.getDepartureTime(),DateStyle.YYYY_MM_DD_HH_MM_SS);
    }
}
