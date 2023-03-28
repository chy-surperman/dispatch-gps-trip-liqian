package com.doudou.dispatch.trip.commons;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ExcelUtil {

	public static HttpServletResponse setExcelResponse(String fileName, HttpServletResponse httpServletResponse)
			throws UnsupportedEncodingException {
		httpServletResponse.setContentType("application/msexcel");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment;fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
		httpServletResponse.addHeader("Pargam", "no-cache");
		httpServletResponse.addHeader("Cache-Control", "no-cache");
		return httpServletResponse;
	}

	public static void setExcelTitle(XSSFWorkbook workbook,Sheet hssfSheet,String title) {
		// 标题
		CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, 32);// 起始行,结束行,起始列,结束列
		hssfSheet.addMergedRegion(callRangeAddress);
		
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);

		Row titleRow = hssfSheet.createRow(0);
		Cell titleCell = titleRow.createCell(0, CellType.STRING);
		titleCell.setCellStyle(style);
		titleCell.setCellValue(title);
		titleRow.setHeightInPoints(20 * 1.5f);
	}
	
	public static void setExcelTitle(XSSFWorkbook workbook,Sheet hssfSheet, int lastCol,String title) {
		// 标题
		CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, lastCol);// 起始行,结束行,起始列,结束列
		hssfSheet.addMergedRegion(callRangeAddress);

		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		style.setFont(font);
		
		Row titleRow = hssfSheet.createRow(0);
		titleRow.setHeightInPoints(20 * 1.5f);
		Cell titleCell = titleRow.createCell(0, CellType.STRING);
		titleCell.setCellStyle(style);
		titleCell.setCellValue(title);
	}

	public static void setMonthHeader(XSSFWorkbook workbook,Sheet hssfSheet,int row,int lastDay) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		Row header = hssfSheet.createRow(row);
		header.setHeightInPoints(20 * 1.2f);

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);

		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("日期");

		for (int i = 1; i <= lastDay; i++) {
			Cell dayCell = header.createCell(i,CellType.STRING);
			dayCell.setCellStyle(style);
			dayCell.setCellValue(i);
		}
		Cell totalCell = header.createCell(lastDay + 1, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("合计");

	}

	public static void setMonthVehicleTripMileageHeader(XSSFWorkbook workbook,Sheet hssfSheet,int row,int lastDay) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		Row header = hssfSheet.createRow(row);
		header.setHeightInPoints(20 * 1.2f);

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);

		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("日期");

		for (int i = 1; i <= lastDay; i++) {
			Cell dayCell = header.createCell(i,CellType.STRING);
			dayCell.setCellStyle(style);
			dayCell.setCellValue(i);
		}
		Cell totalCell = header.createCell(lastDay + 1, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("合计");

		Cell stand = header.createCell(lastDay + 2, CellType.STRING);
		stand.setCellStyle(style);
		stand.setCellValue("趟公里");

		Cell mileage = header.createCell(lastDay + 3, CellType.STRING);
		mileage.setCellStyle(style);
		mileage.setCellValue("班次公里数");

		Cell gpsMileage = header.createCell(lastDay + 4, CellType.STRING);
		gpsMileage.setCellStyle(style);
		gpsMileage.setCellValue("GPS公里数");
	}

	public static void setMonthHeaderContainDriverAndVehicle(XSSFWorkbook workbook,Sheet hssfSheet,int row, int lastDay) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		Row header = hssfSheet.createRow(row);
		header.setHeightInPoints(20 * 1.2f);

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);

		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("日期/司机");

		Cell vehicleCell = header.createCell(1, CellType.STRING);
		vehicleCell.setCellStyle(style);
		vehicleCell.setCellValue("车号");

		for (int i = 1; i <= lastDay; i++) {
			Cell dayCell = header.createCell(1 + i,CellType.STRING);
			dayCell.setCellStyle(style);
			dayCell.setCellValue(i);
		}
		Cell totalCell = header.createCell(lastDay + 2, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("合计");

	}

	public static void setExcelTitle(XSSFWorkbook workbook,Sheet hssfSheet, int startRow, int lastCol,String title) {
		// 标题
		CellRangeAddress callRangeAddress = new CellRangeAddress(startRow, startRow, 0, lastCol);// 起始行,结束行,起始列,结束列
		hssfSheet.addMergedRegion(callRangeAddress);

		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		style.setFont(font);

		Row titleRow = hssfSheet.createRow(startRow);
		titleRow.setHeightInPoints(20 * 1.5f);
		Cell titleCell = titleRow.createCell(0, CellType.STRING);
		titleCell.setCellStyle(style);
		titleCell.setCellValue(title);
	}
	
	public static void setExcelDriverTripHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("司机姓名");

		for (int i = 1; i <= 31; i++) {
			Cell dateCell = header.createCell(i, CellType.STRING);
			dateCell.setCellStyle(style);
			dateCell.setCellValue(i + "");
		}

		Cell totalCell = header.createCell(32, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("司机总趟次");
		
		Cell attendanceCell = header.createCell(33, CellType.STRING);
		attendanceCell.setCellStyle(style);
		attendanceCell.setCellValue("出勤天数");
		
		Cell totalMileageCell = header.createCell(34, CellType.STRING);
		totalMileageCell.setCellStyle(style);
		totalMileageCell.setCellValue("营运里程");
	}
	
	public static void setExcelEarlyShiftHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		header.setHeightInPoints(20 * 1.2f);
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("日期");

		Cell totalCell = header.createCell(1, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("驾驶员");
		
		Cell attendanceCell = header.createCell(2, CellType.STRING);
		attendanceCell.setCellStyle(style);
		attendanceCell.setCellValue("车号");
		
		Cell totalMileageCell = header.createCell(3, CellType.STRING);
		totalMileageCell.setCellStyle(style);
		totalMileageCell.setCellValue("计划时间");
		
		Cell starttagCell = header.createCell(4, CellType.STRING);
		starttagCell.setCellStyle(style);
		starttagCell.setCellValue("方向");
	}
	
	public static void setExcelWorkplanDetialHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		header.setHeightInPoints(20 * 1.2f);
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("车号");

		Cell totalCell = header.createCell(1, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("趟次");
		
		Cell cell2 = header.createCell(2, CellType.STRING);
		cell2.setCellStyle(style);
		cell2.setCellValue("方向");
		
		Cell cell3 = header.createCell(3, CellType.STRING);
		cell3.setCellStyle(style);
		cell3.setCellValue("司机姓名");
		
		Cell cell4 = header.createCell(4, CellType.STRING);
		cell4.setCellStyle(style);
		cell4.setCellValue("计划");
		
		Cell cell5 = header.createCell(5, CellType.STRING);
		cell5.setCellStyle(style);
		cell5.setCellValue("出发");
		
		Cell cell6 = header.createCell(6, CellType.STRING);
		cell6.setCellStyle(style);
		cell6.setCellValue("晚点");
		
		Cell cell7 = header.createCell(7, CellType.STRING);
		cell7.setCellStyle(style);
		cell7.setCellValue("计划间隔");
		
		Cell cell8 = header.createCell(8, CellType.STRING);
		cell8.setCellStyle(style);
		cell8.setCellValue("实际间隔");
		
		Cell cell9 = header.createCell(9, CellType.STRING);
		cell9.setCellStyle(style);
		cell9.setCellValue("到达");
		
		Cell cell10 = header.createCell(10, CellType.STRING);
		cell10.setCellStyle(style);
		cell10.setCellValue("时长");
		
	}
	
	
	public static void setExcelAttendanceRatioHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("司机姓名");

		for (int i = 1; i <= 31; i++) {
			Cell dateCell = header.createCell(i, CellType.STRING);
			dateCell.setCellStyle(style);
			dateCell.setCellValue(i + "");
		}

		Cell totalCell = header.createCell(32, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("司机出勤天数");
	}
	
	public static void setExcelCharteredCountHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("司机姓名");

		for (int i = 1; i <= 31; i++) {
			Cell dateCell = header.createCell(i, CellType.STRING);
			dateCell.setCellStyle(style);
			dateCell.setCellValue(i + "");
		}

		Cell totalCell = header.createCell(32, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("合计次数");
		
		Cell totalMileageCell = header.createCell(33, CellType.STRING);
		totalMileageCell.setCellStyle(style);
		totalMileageCell.setCellValue("包车里程");
	}
	
	public static void setExcelCharteredTripHeader(XSSFWorkbook workbook,Sheet hssfSheet) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		
		Row header = hssfSheet.createRow(1);
		Cell driverNameCell = header.createCell(0, CellType.STRING);
		driverNameCell.setCellStyle(style);
		driverNameCell.setCellValue("司机姓名");

		for (int i = 1; i <= 31; i++) {
			Cell dateCell = header.createCell(i, CellType.STRING);
			dateCell.setCellStyle(style);
			dateCell.setCellValue(i + "");
		}

		Cell totalCell = header.createCell(32, CellType.STRING);
		totalCell.setCellStyle(style);
		totalCell.setCellValue("合计趟次");
		
		Cell totalMileageCell = header.createCell(33, CellType.STRING);
		totalMileageCell.setCellStyle(style);
		totalMileageCell.setCellValue("包车里程");
	}

}
