package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.Driver;
import com.doudou.dispatch.trip.api.entities.TVehicle;
import com.doudou.dispatch.trip.api.feigns.BaseDataFeignClient;
import com.doudou.dispatch.trip.api.mappers.BaseDataMapper;
import com.doudou.dispatch.trip.api.services.BaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("baseDataService")
public class BaseDataServiceImpl implements BaseDataService {

    @Autowired
    private BaseDataFeignClient baseDataFeignClient;

    @Autowired
    private BaseDataMapper baseDataMapper;

    private Map<String,List<Driver>> companyDriverMaps = new ConcurrentHashMap<>();

    private Map<String,List<TVehicle>> companyVehicleMaps = new ConcurrentHashMap<>();

    @Override
    public Driver getDriverById(String driverId,String company) {
        List<Driver> drivers = this.getDrivers(null, company);
        for(Driver driver:drivers){
            if(driver.getDriverId().equals(driverId)){
                return driver;
            }
        }
        return null;
    }

    @Override
    public TVehicle getVehicleById(String vehicleId,String company) {
        List<TVehicle> vehicles = this.getVehicles(null, company);
        for(TVehicle vehicle:vehicles){
            if(vehicle.getVehicleId().equals(vehicleId)){
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public List<String> getRouteMsgs(String company) {
        List<String> routeNames = baseDataMapper.selectRouteNames(company);
//        Iterator<String> iterator = routeNames.iterator();
//        while (iterator.hasNext()){
//            String routeName = iterator.next();
//            if(routeName.toUpperCase().charAt(0) != 'X'){
//                iterator.remove();
//            }
//        }
        routeNames.add("城乡1号线");
        return routeNames;
    }

    @Override
    public List<TVehicle> getVehicles(String routeName, String company) {
//        if(StringUtils.isNotEmpty(routeName)){
//            return baseDataFeignClient.getRouteVehicle(routeName).getResult();
//        }
//        List<TVehicle> tVehicles = companyVehicleMaps.get(company);
//        if(tVehicles == null){
//            tVehicles = baseDataFeignClient.getCompanyVehicle(company).getResult();
//            companyVehicleMaps.put(company,tVehicles);
//        }
//        return tVehicles;
        return this.baseDataMapper.selectCsCityVehicle();
    }

    @Override
    public List<Driver> getDrivers(String routeName, String company) {
//        if(StringUtils.isNotEmpty(routeName)){
//            return baseDataFeignClient.getRouteDriver(routeName).getResult();
//        }
//        List<Driver> drivers = companyDriverMaps.get(company);
//        if(null == drivers){
//            drivers = baseDataFeignClient.getCompanyDriver(company).getResult();
//            companyDriverMaps.put(company,drivers);
//        }
        return this.baseDataMapper.selectCsCityDriver();
    }
}
