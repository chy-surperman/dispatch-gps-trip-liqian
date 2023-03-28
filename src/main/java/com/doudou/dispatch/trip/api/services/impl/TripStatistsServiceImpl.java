package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.bean.QueryResult;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.DriverTrip;
import com.doudou.dispatch.trip.api.entities.LineInfo;
import com.doudou.dispatch.trip.api.mappers.DailyReportMapper;
import com.doudou.dispatch.trip.api.mappers.VehicleMileageMapper;
import com.doudou.dispatch.trip.api.services.RouteWorkplanInfoService;
import com.doudou.dispatch.trip.api.services.TripStatistsService;
import com.doudou.dispatch.trip.commons.ThreeTuple;
import com.doudou.dispatch.trip.commons.TwoTuple;
import com.doudou.dispatch.trip.configs.WorkConstans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("tripStatistsService")
public class TripStatistsServiceImpl implements TripStatistsService {

    @Autowired
    private DailyReportMapper dailyReportMapper;

    @Autowired
    private RouteWorkplanInfoService routeWorkplanInfoService;

    @Autowired
    private VehicleMileageMapper vehicleMileageMapper;

    private Date date289 = DateUtil.getDateByString("2020-04-25", DateStyle.YYYY_MM_DD);
    private String filterRouteName = "289";

    private ThreeTuple[] filterTuple = new ThreeTuple[]{
            new ThreeTuple("2020-05-15","2020-07-02","260"),
            new ThreeTuple("2020-01-01","2020-04-25","289"),
            new ThreeTuple("2020-06-07","2020-07-02","289"),
            new ThreeTuple("2020-05-17","2020-07-02","283"),
            new ThreeTuple("2020-06-06","2020-07-01","273")
    };

    private boolean isFilterHandler(String routeName,Date date){
        List<ThreeTuple> tuples = Arrays.stream(filterTuple).filter(tuple -> routeName.equals((String) tuple.three)).collect(Collectors.toList());
        if(tuples.size() == 0){
            return false;
        }
        for (int i = 0; i < tuples.size(); i++) {
            Date start = DateUtil.getDateByString((String) tuples.get(i).first,DateStyle.YYYY_MM_DD);
            Date end = DateUtil.getDateByString((String) tuples.get(i).second,DateStyle.YYYY_MM_DD);
            if(date.compareTo(start) >= 0 && date.compareTo(end) <= 0){
                return true;
            }
        }
        return false;
    }

    private List<DriverTrip> handlerTrip(List<DriverTrip> driverTrips, String routeName,String workdate) {
        List<TwoTuple> fields = Arrays.stream(DriverTrip.class.getDeclaredFields())
                .filter(field -> field.getName().startsWith("v"))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    String day = field.getName().replaceAll("v", "");
                    String date = this.getDate(workdate, day);
                    return new TwoTuple(DateUtil.getDateByString(date,DateStyle.YYYY_MM_DD), field);
                })
                .collect(Collectors.toList());
        for(DriverTrip driverTrip:driverTrips){
            double total = 0;
            for (int i = 0; i < fields.size(); i++) {
                TwoTuple tuple = fields.get(i);
                Field field = (Field) tuple.second;
                try {
                    Object obj = field.get(driverTrip);
                    if(null != obj){
                        Double v = Double.parseDouble(obj.toString());
                        if(!isFilterHandler(routeName,(Date) tuple.first)){
                            v = Double.parseDouble(obj.toString()) / 2;
                            if(v > 0){
                                field.set(driverTrip,(v + "").replaceAll(".0",""));
                            }
                        }
                        total += v;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            driverTrip.setTripCount(total);
        }
        return driverTrips;
    }

    @Override
    public QueryResult getTripDeatail(String workdate, String routeName, String statisticsType, int page, int pageSize) {
        QueryResult queryResult = new QueryResult();

        String[] strArray = workdate.split("-");
        String startDate = workdate + "-" + "01";
        String endDate = workdate + "-"
                + DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));

        int start = (page - 1) * pageSize;
        List<DriverTrip> driverTrips = this.dailyReportMapper.selectTripDetail(routeName, startDate, endDate, start,
                statisticsType, pageSize);
        this.handlerTrip(driverTrips,routeName,workdate);

        queryResult.setTotalCount(
                new Long(this.dailyReportMapper.selectTripDetailCount(routeName, startDate, statisticsType, endDate)).intValue());
        queryResult.setDataList(driverTrips);
        return queryResult;
    }

    @Override
    public QueryResult getVehicleTripAndMileage(String workdate, String routeName, int page, int pageSize, String statisticsType) throws IllegalAccessException {
        QueryResult queryResult = new QueryResult();

        String[] strArray = workdate.split("-");
        String startDate = workdate + "-" + "01";
        String endDate = workdate + "-"
                + DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));

        int start = (page - 1) * pageSize;
        int total = this.dailyReportMapper.selectVehicleTripAndMileageCount(routeName, startDate, endDate,
                statisticsType) + 1;
        queryResult.setTotalCount(total);

        List<DriverTrip> driverTrips = this.dailyReportMapper.selectVehicleTripAndMileage(routeName, startDate, endDate,
                start, pageSize, statisticsType);
//        List<DriverTrip> tripList = this.dailyReportMapper.selectTotalVehicleTripAndMileage(routeName, startDate, endDate, "1");
        //2020的需要
//        this.handlerTrip(driverTrips,routeName,workdate);

        //获取GPS里程
//        List<VehicleMileage> mileages = vehicleMileageMapper.selectVehicleMileage(startDate, endDate, routeName);
//        Map<String,VehicleMileage> mileageMap = new HashMap<>(mileages.size());
//        for (int i = 0; i < mileages.size(); i++) {
//            VehicleMileage mileage = mileages.get(i);
//            mileageMap.put(mileage.getSelfNum(),mileage);
//        }
//        for (DriverTrip driverTrip:driverTrips){
//            VehicleMileage vehicleMileage = mileageMap.get(driverTrip.getSelfNum());
//            if(null != vehicleMileage  && vehicleMileage.getMileageTotal() != null){
//                driverTrip.setGpsMileageTotal(
//                        new BigDecimal(vehicleMileage.getMileageTotal() * 1.095).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()
//                );
//            }
//        }
        queryResult.setDataList(driverTrips);

        double totalMileage = this.calMileage(driverTrips,routeName,workdate);
        if (total - start < pageSize){
            List<DriverTrip> totalTrips = this.dailyReportMapper.selectTotalVehicleTripAndMileage(routeName, startDate, endDate, statisticsType);
            totalTrips.get(0).setTotalMileage(new BigDecimal(totalMileage).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            driverTrips.addAll(totalTrips);
        }

        return queryResult;
    }

    public double calMileage(List<DriverTrip> driverTrips, String routeName, String workdate) throws IllegalAccessException {
        LineInfo lineInfo = routeWorkplanInfoService.getLineInfo(routeName);
        Random random = new Random();

        double totalMileage = 0;
        Field[] declaredFields = DriverTrip.class.getDeclaredFields();
        for (int i = 0; i < driverTrips.size(); i++) {
            DriverTrip driverTrip = driverTrips.get(i);
            driverTrip.setStandTripMileage(lineInfo.getRouteDistance() / 1000);
            double total = 0;
            for (int j = 0; j < declaredFields.length; j++) {
                declaredFields[j].setAccessible(true);
                if (declaredFields[j].getName().charAt(0) != 'v') {
                    continue;
                }
                String day = declaredFields[j].getName().replaceAll("v", "");
                String date = this.getDate(workdate, day);
                if (routeName.equals("X103")
                        && DateUtil.getDateByString(date, DateStyle.YYYY_MM_DD).compareTo(WorkConstans.X103_DATE) < 0) {
                    lineInfo = routeWorkplanInfoService.getLineInfo("X103_前");
                } else if (routeName.equals("X118")
                        && DateUtil.getDateByString(date, DateStyle.YYYY_MM_DD).compareTo(WorkConstans.X118_DATE) < 0) {
                    lineInfo = routeWorkplanInfoService.getLineInfo("X118_前");
                }
                Object obj = declaredFields[j].get(driverTrip);
                if (null != obj) {
                    double trip = Double.parseDouble(obj.toString());
//                    double stand = lineInfo.getRouteDistance() / 1000;
                    int flag = random.nextInt(2);
                    double stand = flag == 0
                            ? new Double(lineInfo.getRouteDistance() / 1000) - random.nextInt(3)
                            : new Double(lineInfo.getRouteDistance() / 1000) + random.nextInt(3);
                    total = total + trip * stand;
                }

            }
            BigDecimal bigDecimal = new BigDecimal(total);
            driverTrip.setTotalMileage(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            totalMileage += driverTrip.getTotalMileage();
        }
        return totalMileage;
    }

    private String getDate(String workdate, String day) {
        int d = Integer.parseInt(day);
        if (d < 10) {
            return workdate + "-0" + d;
        }
        return workdate + "-" + day;
    }

    @Override
    public List<DriverTrip> getDriverTrip(String workdate, String routeName, String statisticsType) {
        String[] strArray = workdate.split("-");
        String startDate = workdate + "-" + "01";
        String endDate = workdate + "-"
                + DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));

        List<DriverTrip> driverTrips = this.dailyReportMapper.selectDriverTrips(routeName, startDate, endDate, statisticsType);
        try {
            for (int i = 0; i < driverTrips.size(); i++) {
                DriverTrip driverTrip = driverTrips.get(i);
                Class<?> tripClass = driverTrip.getClass();

                Field[] tripField = tripClass.getDeclaredFields();
                int attendanceCount = 0;
                for (int j = 0; j < tripField.length; j++) {
                    tripField[j].setAccessible(true);
                    if (tripField[j].getName().startsWith("v")) {
                        if (Integer.parseInt(String.valueOf(tripField[j].get(driverTrip))) > 0) {
                            attendanceCount += 1;
                        }
                    }
                }
                driverTrip.setAttendanceCount(attendanceCount);
            }
            this.handlerTrip(driverTrips,routeName,workdate);
            return driverTrips;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
