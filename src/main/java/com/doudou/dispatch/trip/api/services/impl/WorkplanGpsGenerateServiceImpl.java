package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.Workplan;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.dispatch.gps.commons.utils.GPSUtil;
import com.doudou.dispatch.trip.api.entities.GpsTemplate;
import com.doudou.dispatch.trip.api.entities.LineInfo;
import com.doudou.dispatch.trip.api.services.GpsTemplateService;
import com.doudou.dispatch.trip.api.services.RouteWorkplanInfoService;
import com.doudou.dispatch.trip.api.services.WorkplanGpsGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("workplanGpsGenerateService")
public class WorkplanGpsGenerateServiceImpl implements WorkplanGpsGenerateService {

    @Autowired
    private GpsTemplateService gpsTemplateService;

    private Random random = new Random();

    @Autowired
    private RouteWorkplanInfoService routeWorkplanInfoService;

    @Override
    public int getGpsFreq(String routeName){
        LineInfo lineInfo = routeWorkplanInfoService.getLineInfo(routeName);
        if(null != lineInfo){
            return lineInfo.getGpsFreq();
        }
        return RouteWorkplanInfoService.GPS_REPORT_FREQ;
    }

    @Override
    public List<GenerateGps> generateGps(Workplan workplan) {

        //计算运行时长
        Date arrival = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getArrivalString(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        Date departure = DateUtil.getDateByString(workplan.getDateString() + " " + workplan.getDepartureString(),DateStyle.YYYY_MM_DD_HH_MM_SS);

        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长

        List<GpsTemplate> gpsTemplates = gpsTemplateService.getGpsTemplate(workplan);
        if(null == gpsTemplates){
            return null;
        }

        GenerateGps firstGps = this.createPoint(gpsTemplates.get(0),workplan.getVehicleId());
        firstGps.setCreateTime(departure);
        firstGps.setTime(DateUtil.getDateByDs(firstGps.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));

        List<GenerateGps> generateGpsList = new ArrayList<>(gpsTemplates.size());
        generateGpsList.add(firstGps);

        int freq = this.getGpsFreq(workplan.getRouteName());
        int gpsSize = runTime / freq;

        boolean isAddGps = gpsTemplates.size() <= gpsSize ? true : false;
        List<GenerateGps> generateGpss = isAddGps
                ? this.generateAddGps(generateGpsList,gpsTemplates,workplan.getVehicleId(),departure,freq,arrival)
                : this.generateSubGps(generateGpsList,gpsTemplates,workplan.getVehicleId(),departure,freq,arrival);
        return generateGpss;
//        return freq == RouteWorkplanInfoService.GPS_REPORT_FREQ_28 ? this.handlerGpsSpeed(generateGpss) : generateGpss;
    }

    private List<GenerateGps> handlerGpsSpeed(List<GenerateGps> generateGpss){
        for (int i = 1; i < generateGpss.size(); i++) {
            GenerateGps before = generateGpss.get(i - 1);
            GenerateGps current = generateGpss.get(i);
            double distance = GPSUtil.getDistance(current.getLatitude(), current.getLongitude(), before.getLatitude(), before.getLongitude());
            int diff = this.timeDiffSecond(before.getCreateTime(),current.getCreateTime());

            int randomValue = random.nextInt(2);
            float speed = new Double(Math.floor((distance / diff) * 3.6)).floatValue();
            if(speed >= 6 && speed < 35){
                speed = randomValue == 0 ? speed - random.nextInt(2) : speed + random.nextInt(2);
            }else if(speed >= 35 && speed <= 41){
                speed = randomValue == 0 ? speed - random.nextInt(3) : speed + random.nextInt(3);
            }else if(speed >= 46){
                speed = 40 + random.nextInt(8);
            }
            current.setSpeed(speed);
        }
        return generateGpss;
    }

    private List<GenerateGps> generateSubGps(List<GenerateGps> generateGpsList,
                                             List<GpsTemplate> gpsTemplates,
                                             String vehicleId,
                                             Date departure,
                                             int freq,
                                             Date arrival){
        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长
        int gpsSize = runTime / freq;
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
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId,freq);
                generateGpsList.add(point);
            }
        }

        return generateGpsList;
    }

    private List<GenerateGps> generateAddGps(List<GenerateGps> generateGpsList,
                                             List<GpsTemplate> gpsTemplates,
                                             String vehicleId,
                                             Date departure,
                                             int freq,
                                             Date arrival){
        int runTime = new Long((arrival.getTime() - departure.getTime()) / 1000).intValue();//运行时长
        int gpsSize = runTime / freq;
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
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId,freq);
                generateGpsList.add(point);
                //速度为0点重复添加
                for (int j = 0; j < repeat && addCount > 0; j++) {
                    point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId,freq);
                    generateGpsList.add(point);
                    addCount--;
                }
            }else{
                GenerateGps point = this.createPoint(gpsTemplates.get(i), generateGpsList.get(generateGpsList.size() - 1), vehicleId,freq);
                generateGpsList.add(point);
            }
        }

        return generateGpsList;
    }

    private GenerateGps createPoint(GpsTemplate gpsTemplate,GenerateGps preGps,String vehicleId,int freq){
        GenerateGps gps = this.createPoint(gpsTemplate, vehicleId);

        int interval = freq;
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
            speed = randomValue == 0 ? gpsTemplate.getSpeed() - random.nextInt(2) : gpsTemplate.getSpeed() + random.nextInt(2);
        }else if(gpsTemplate.getSpeed() >= 35){
            speed = randomValue == 0 ? gpsTemplate.getSpeed() - random.nextInt(3) : gpsTemplate.getSpeed() + random.nextInt(3);
        }
        generateGps.setSpeed(speed);
        return generateGps;
    }

    private int timeDiff(Date start,Date end){
        return new Long((end.getTime() - start.getTime()) / 1000 / 60).intValue();
    }

    private int timeDiffSecond(Date start,Date end){
        return new Long((end.getTime() - start.getTime()) / 1000).intValue();
    }

    private Date getWorkplanDepartue(Workplan workplan){
        return DateUtil.getDateByString(workplan.getDate() + " " + workplan.getDepartureTime(), DateStyle.YYYY_MM_DD_HH_MM_SS);
    }

}
