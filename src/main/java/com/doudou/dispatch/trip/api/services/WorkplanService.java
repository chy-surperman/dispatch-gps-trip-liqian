package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.*;

import java.util.List;

public interface WorkplanService {

    public List<TVehicle> queryWorkplanVehicles(String date,String routeName);

    public List<Workplan> queryWorkplans(QueryWorkplan queryWorkplan);

    public List<GpsLocus> getWorkplanGps(String startDate, String endDate, String vehicleId, String routeName);

    public boolean saveCreateWorkplanGps(CreateWorkplan workplan) throws Exception;

    public int saveManulWorkplanChangeRecode(WorkplanChangeRecode changeRecord) throws Exception;

    public int saveManulWorkplanChangeRecodeWithoutGPS(WorkplanChangeRecode changeRecord) throws Exception;

    public boolean deleteWorkplan(long id) throws Exception;

    public boolean deleteWorkplanWithoutGps(long id) throws Exception;

    public List<BigDailyWorkplanReport> getBigDailyWorkplanReport(String routeName, String date, String isPrint);

}
