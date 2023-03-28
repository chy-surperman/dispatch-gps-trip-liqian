package com.doudou.dispatch.trip.api.mappers;

import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.BigDailyWorkplan;
import com.doudou.dispatch.trip.api.entities.CreateWorkplan;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.doudou.dispatch.trip.api.entities.TVehicle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkplanMapper {

    public List<BigDailyWorkplan> selectBigDailyWorkplanReport(@Param("routeName") String routeName,
                                                               @Param("date") String date,
                                                               @Param("isThroughMonitorSite") String ThroughMonitorSite);

    public List<TVehicle> selectWorkplanVehicles(@Param("routeName") String routeName,
                                                       @Param("date") String date);

    public CreateWorkplan selectById(long id);

    public int deleteById(long id);

    public List<Workplan> selectByQueryWorkplan(@Param("queryWorkplan") QueryWorkplan queryWorkplan);

    public List<Workplan> selectDriverWorkplans(@Param("driverId") String driverId,@Param("date") String date);

    public int insert(CreateWorkplan workplan);

    public int update(CreateWorkplan workplan);
}
