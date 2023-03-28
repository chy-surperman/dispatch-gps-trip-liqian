package com.doudou.dispatch.trip.configs;

import com.doudou.dispatch.trip.api.mappers.BaseDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HandlerDriverRepeat implements ApplicationListener<ApplicationStartedEvent>,Runnable{

    @Autowired
    private BaseDataMapper baseDataMapper;

    @Override
    public void run() {

    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
//        List<String> driverNames = baseDataMapper.selectRepeatDriver();
//        for (int i = 0; i < driverNames.size(); i++) {
//            List<Driver> drivers = baseDataMapper.selectDriverByName(driverNames.get(i));
//            Driver driver = drivers.get(0);
//            for (int j = 1; j < drivers.size(); j++) {
//                log.info("" + baseDataMapper.updateWorkplan(drivers.get(j).getDriverId(),driver.getDriverId()));
//                log.info("" + baseDataMapper.deleteById(drivers.get(j).getId()));
//            }
//            log.info(driverNames.get(i));
//        }
    }
}
