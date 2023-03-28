package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.QueryResult;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.DriverTrip;
import com.doudou.dispatch.trip.api.entities.RouteWorkplanTrip;
import com.doudou.dispatch.trip.api.services.TripStatistsService;
import com.doudou.dispatch.trip.commons.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 趟次统计
 * @author ouyang
 *
 */
@Controller
@RequestMapping("/report/down")
public class DownWorkplanController {

	@Autowired
	private TripStatistsService tripStatistsService;
	
	/**
	 * 司机趟次统计
	 * @param routeName
	 * @return
	 */
	@RequestMapping("/driver")
	public void driverWorkplan(HttpServletResponse httpServletResponse,String workdate, String routeName) {
		
		if(StringUtils.isAnyEmpty(workdate)) {
			return;
		}
		
		String[] strArray = workdate.split("-");
		int lastDay = DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));

		List<DriverTrip> workplanTrips = tripStatistsService.getDriverTrip(workdate, routeName, "1");
		if(null == workplanTrips) {
			return;
		}
		List<RouteWorkplanTrip> routeWorkplanTrips = this.getRouteWorkplanTrips(routeName,workplanTrips);

		String fileName = "%s线" + workdate + "驾驶员趟次统计表.xlsx";
		this.downWorkplanTrip(httpServletResponse, routeWorkplanTrips, lastDay, "driver", fileName);
	}
	
	/**
	 * 司机趟次统计
	 * @param routeName
	 * @return
	 */
	@RequestMapping("/vehicle")
	public void vehicleWorkplan(HttpServletResponse httpServletResponse,String workdate, String routeName) throws IllegalAccessException {
		
		if(StringUtils.isAnyEmpty(workdate)) {
			return;
		}
		
		String[] strArray = workdate.split("-");
		int lastDay = DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));

		QueryResult queryResult = tripStatistsService.getVehicleTripAndMileage(workdate, routeName, 1, 1000, "1");
		if(null == queryResult || queryResult.getDataList() == null || queryResult.getDataList().size() == 0) {
			return;
		}
		List<RouteWorkplanTrip> routeWorkplanTrips = this.getRouteWorkplanTrips(routeName,(List<DriverTrip>) queryResult.getDataList());

		String fileName = "%s线" + workdate + "车辆趟次统计表.xlsx";
		this.downWorkplanTrip(httpServletResponse, routeWorkplanTrips, lastDay, "vehicle", fileName);
	}
	
	/**
	 * 司机车辆趟次统计
	 * @param routeName
	 * @return
	 */
	@RequestMapping("/driverVehicle")
	public void driverVehicleWorkplan(HttpServletResponse httpServletResponse,String workdate, String routeName) {
		if(StringUtils.isAnyEmpty(workdate)) {
			return;
		}
		String[] strArray = workdate.split("-");
		String startDate = workdate + "-" + "01";
		int lastDay = DateUtil.getDaysByYearMonth(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));
		String endDate = workdate + "-" + lastDay;

		QueryResult queryResult = tripStatistsService.getTripDeatail(workdate, routeName, "1", 1, 1000);
		if(null == queryResult || queryResult.getDataList() == null || queryResult.getDataList().size() == 0) {
			return;
		}
		List<RouteWorkplanTrip> routeWorkplanTrips = this.getRouteWorkplanTrips(routeName,(List<DriverTrip>) queryResult.getDataList());
		String fileName = "%s线" + workdate + "驾驶员车辆趟次统计表.xlsx";
		this.downWorkplanTrip(httpServletResponse, routeWorkplanTrips, lastDay, "driver/vehicle", fileName);
	}


	private void downWorkplanTrip(HttpServletResponse httpServletResponse,List<RouteWorkplanTrip> routeWorkplanTrips,int lastDay,String type,String fileName) {
		ServletOutputStream sos = null;
		XSSFWorkbook workbook = null;
		try {
			sos = httpServletResponse.getOutputStream();
			ExcelUtil.setExcelResponse(fileName, httpServletResponse);

			//创建excel
			workbook = new XSSFWorkbook();
			Sheet hssfSheet = workbook.createSheet();

			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);

			//确定列
			int lastCol = lastDay + 2;
			if("driver/vehicle".equals(type)) {
				lastCol = lastDay + 3;
			}
			if("vehicle".equals(type)) {
				lastCol = lastDay + 3;
			}
			//设置列宽
			for (int i = 0; i < lastCol; i++) {
				hssfSheet.setColumnWidth(i, 150 * 16);
			}

			int routeStartRow = 0;
			for (int i = 0; i < routeWorkplanTrips.size(); i++) {
				RouteWorkplanTrip routeWorkplanTrip = routeWorkplanTrips.get(i);

				String title = String.format(fileName, routeWorkplanTrip.getRouteName());

				//设置title
				ExcelUtil.setExcelTitle(workbook, hssfSheet, routeStartRow, lastCol, title.replace(".xlsx", ""));

				//设置header
				if("driver/vehicle".equals(type)) {
					ExcelUtil.setMonthHeaderContainDriverAndVehicle(workbook, hssfSheet, routeStartRow + 1, lastDay);
				}else if("vehicle".equals(type)){
					ExcelUtil.setMonthVehicleTripMileageHeader(workbook, hssfSheet, routeStartRow + 1, lastDay);
				}else {
					ExcelUtil.setMonthHeader(workbook, hssfSheet, routeStartRow + 1, lastDay);
				}

				List<DriverTrip> workplanTrips = routeWorkplanTrip.getWorkplanTrips();
				for (int k = 0; k < workplanTrips.size(); k++) {
					Row row = hssfSheet.createRow(routeStartRow + 2 + k);
					row.setHeightInPoints(20 * 1.1f);

					DriverTrip revenue = workplanTrips.get(k);
					int dateOffset = 0;

					Cell firstCell = row.createCell(0, CellType.STRING);
					firstCell.setCellStyle(style);
					if(StringUtils.isEmpty(type) || type.equals("vehicle")) {
						firstCell.setCellValue(revenue.getSelfNum());
					}else if(type.equals("driver")) {
						firstCell.setCellValue(revenue.getDriverName());
					}else if(type.equals("driver/vehicle")) {
						dateOffset = 1;
						firstCell.setCellValue(revenue.getDriverName());

						Cell secondCell = row.createCell(1, CellType.STRING);
						secondCell.setCellStyle(style);
						secondCell.setCellValue(revenue.getSelfNum());
					}

					Class<?> tripClass = revenue.getClass();
					Field[] tripField = tripClass.getDeclaredFields();
					for (int j = 0; j < tripField.length; j++) {
						tripField[j].setAccessible(true);
						if(tripField[j].getName().startsWith("v")) {
							int day = Integer.parseInt(tripField[j].getName().replace("v", ""));
							if(day <= lastDay) {
								Cell dayCell = row.createCell(day + dateOffset, CellType.STRING);
								dayCell.setCellStyle(style);
								Object obj = tripField[j].get(revenue);
								if(null != obj)
									dayCell.setCellValue(String.valueOf(obj));
								else
									dayCell.setCellValue("0");
							}
						}
					}
					Cell lastCell = row.createCell(lastDay + 1 + dateOffset, CellType.STRING);
					lastCell.setCellStyle(style);
					lastCell.setCellValue(String.valueOf(revenue.getTripCount()));

					if(StringUtils.isEmpty(type) || type.equals("vehicle")) {
						Cell stand = row.createCell(lastDay + 2 + dateOffset, CellType.STRING);
						stand.setCellStyle(style);
						stand.setCellValue(String.valueOf(revenue.getStandTripMileage()));

						Cell mileage = row.createCell(lastDay + 3 + dateOffset, CellType.STRING);
						mileage.setCellStyle(style);
						mileage.setCellValue(String.valueOf(revenue.getTotalMileage()));

						Cell gpsMileage = row.createCell(lastDay + 4 + dateOffset, CellType.STRING);
						gpsMileage.setCellStyle(style);
						gpsMileage.setCellValue(String.valueOf(revenue.getGpsMileageTotal()));
					}
				}

				//下一条线路的起始行
				routeStartRow = routeStartRow + workplanTrips.size() + 2 + 2;
			}
			workbook.write(sos);
			sos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != sos)
					sos.close();
				if(null != workbook)
					workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<RouteWorkplanTrip> getRouteWorkplanTrips(String routeName,List<DriverTrip> workplanTrips){
		List<RouteWorkplanTrip> routeWorkplanTrips = new ArrayList<>();
		RouteWorkplanTrip routeWorkplanTrip = new RouteWorkplanTrip();
		routeWorkplanTrip.setRouteName(routeName);
		routeWorkplanTrip.setWorkplanTrips(workplanTrips);
		routeWorkplanTrips.add(routeWorkplanTrip);
		return routeWorkplanTrips;
	}
}
