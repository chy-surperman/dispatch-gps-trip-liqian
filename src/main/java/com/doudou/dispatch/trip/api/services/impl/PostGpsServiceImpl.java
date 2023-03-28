package com.doudou.dispatch.trip.api.services.impl;

import com.dispatch.gps.commons.entities.GenerateGps;
import com.dispatch.gps.commons.entities.GpsLocus;
import com.dispatch.gps.commons.entities.WorkplanPostGps;
import com.dispatch.gps.commons.utils.DateStyle;
import com.dispatch.gps.commons.utils.DateUtil;
import com.dispatch.gps.commons.utils.GPSUtil;
import com.doudou.dispatch.trip.api.entities.Gpsreport;
import com.doudou.dispatch.trip.api.mappers.GpsreportMapper;
import com.doudou.dispatch.trip.api.mappers.PostGpsMapper;
import com.doudou.dispatch.trip.api.services.GpsTableManagerService;
import com.doudou.dispatch.trip.api.services.PostGpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("postGpsService")
public class PostGpsServiceImpl implements PostGpsService {

	@Autowired
	private PostGpsMapper postGpsMapper;

	@Autowired
	private GpsreportMapper gpsreportMapper;
	
	@Autowired
	private GpsTableManagerService gpsTableManagerService;

	@Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
	@Override
	public boolean savePostGps(WorkplanPostGps workplanPostGps) {
		String tableName = gpsTableManagerService.getGpsTableName(workplanPostGps.getDate());
		
		List<GenerateGps> generateGpss = workplanPostGps.getGpsLists();
		for(GenerateGps gps:generateGpss) {
			gps.setSign(workplanPostGps.getSign());
		}
		
		return postGpsMapper.savePostGps(workplanPostGps, tableName) > 0;
	}

	@Override
	public boolean confirmPostGps(String date, String sign) {
		String tableName = gpsTableManagerService.getGpsTableName(date);
		return postGpsMapper.comfirePostGps(sign, tableName) > 0;
	}

	@Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
	@Override
	public boolean cancelPostGps(String date, String sign) {
		String tableName = gpsTableManagerService.getGpsTableName(date);
		return postGpsMapper.cancelPostGps(sign, tableName) >= 0;
	}

	@Override
	public boolean moveSlaveGps(String startDate, String endDate, String vehicleId, String sign) {
		Date d = DateUtil.getDateByString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS);
		String gpsTableName = gpsTableManagerService.getGpsTableName(d);
		return postGpsMapper.moveSlaveGpsInsert(gpsTableName, startDate, endDate, vehicleId, sign) > 0;
	}

	@Override
	public boolean deleteGps(Date startTime, Date endTime, String vehicleId) {
		String gpsTableName = gpsTableManagerService.getGpsTableName(startTime);
		return gpsreportMapper.deleteGps(gpsTableName,startTime, endTime, vehicleId) > 0;
	}

	@Override
	public List<GpsLocus> getVehicleLocus(Date startTime, Date endTime, String vehicleId) {

		List<GpsLocus> points = new ArrayList<>();
		//获取查询表名
		String gpsTableName = gpsTableManagerService.getGpsTableName(startTime);
		List<Gpsreport> gpsreports = this.gpsreportMapper.selectTime(gpsTableName,startTime, endTime, vehicleId);

		if(gpsreports.size() > 0){
			for (int i = 1; i < gpsreports.size(); i++) {
				Gpsreport gpsreport = gpsreports.get(i);
				GpsLocus point = new GpsLocus(Double.parseDouble(gpsreport.getSpeed()));
				double[] gpsPoint = GPSUtil.gps84_To_Gcj02(new Double(gpsreport.getLatitude()),new Double(gpsreport.getLongitude()));
				point.setLatitude(gpsPoint[0]);
				point.setLongitude(gpsPoint[1]);
				point.setReportTime(DateUtil.getDateByDs(gpsreport.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
				points.add(point);
			}
		}
		return points;
	}
}
