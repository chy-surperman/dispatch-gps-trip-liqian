package com.doudou.dispatch.trip;

import com.dispatch.gps.commons.bean.QueryResult;
import com.dispatch.gps.commons.utils.DateUtil;
import com.doudou.dispatch.trip.api.entities.DriverTrip;
import com.doudou.dispatch.trip.api.services.TripStatistsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@Component
public class ExportVehicleExcelTableWork implements ApplicationListener<ApplicationStartedEvent>,Runnable{

    @Autowired
    private TripStatistsService tripStatistsService;

    private String exportFilePath(String dire,String fileName){
        return dire + "\\" + fileName + ".xlsx";
    }

    private String getRouteDire(String dire,String routeName){
        String path = dire + "\\" + routeName;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    private String getMonth(String year,int moth){
        if(moth < 10){
            return year + "-0" + moth;
        }
        return year + "-" + moth;
    }

    private String getFileName(String routeName,String month){
        return routeName + "_" + month;
    }

    private void exportExcel(String year,String inputPath,String outPath) throws IllegalAccessException {
        File inputFile = new File(inputPath);
        File[] files = inputFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            String routeName = files[i].getName();
            for (int j = 1; j <= 12; j++) {
                String month = this.getMonth(year, j);

                int dayCols = DateUtil.getDaysByYearMonth(Integer.parseInt(year),j);
                String fileName = this.getFileName(routeName,month);
                String exportFilePath = this.exportFilePath(this.getRouteDire(outPath, routeName), fileName);
                QueryResult queryResult = tripStatistsService.getVehicleTripAndMileage(month, routeName, 1, 1000, "1");

                this.doExportVehicleExcel(exportFilePath,(List<DriverTrip>) queryResult.getDataList(),fileName,dayCols);

                log.info(fileName);
            }
        }
        log.info("finish...");
    }

    public void doExportVehicleExcel(String outPath, List<DriverTrip> driverTrips, String title,int dayCols) {
        XSSFWorkbook workbook = null;
        try {

            workbook = new XSSFWorkbook();
            Sheet hssfSheet = workbook.createSheet();

            // 标题
            CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, dayCols+2);// 起始行,结束行,起始列,结束列
            hssfSheet.addMergedRegion(callRangeAddress);

            XSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);

            Row titleRow = hssfSheet.createRow(0);
            Cell titleCell = titleRow.createCell(0, CellType.STRING);
            titleCell.setCellStyle(style);
            titleCell.setCellValue(title);

            // 表头
            Row header = hssfSheet.createRow(1);
            Cell driverNameCell = header.createCell(0, CellType.STRING);
            driverNameCell.setCellStyle(style);
            driverNameCell.setCellValue("车号");

            for (int i = 1; i <= dayCols; i++) {
                Cell dateCell = header.createCell(i, CellType.STRING);
                dateCell.setCellStyle(style);
                dateCell.setCellValue(i + "");
            }

            Cell totalCell = header.createCell(dayCols + 1, CellType.STRING);
            totalCell.setCellStyle(style);
            totalCell.setCellValue("合计");

            Cell mileageCell = header.createCell(dayCols + 2, CellType.STRING);
            mileageCell.setCellStyle(style);
            mileageCell.setCellValue("公里数");

            Field[] tripField = DriverTrip.class.getDeclaredFields();
            for (int i = 0; i < driverTrips.size(); i++) {
                Row row = hssfSheet.createRow(2 + i);

                DriverTrip driverTrip = driverTrips.get(i);
                row.createCell(0, CellType.STRING).setCellValue(driverTrip.getSelfNum());

                for (int j = 0; j < tripField.length; j++) {
                    tripField[j].setAccessible(true);
                    if(tripField[j].getName().startsWith("v")) {
                        int day = Integer.parseInt(tripField[j].getName().replace("v", ""));
                        row.createCell(day, CellType.STRING).setCellValue(String.valueOf(tripField[j].get(driverTrip)));
                    }
                }
                row.createCell(32, CellType.STRING).setCellValue(driverTrip.getTripCount());
                row.createCell(33, CellType.STRING).setCellValue(driverTrip.getTotalMileage());
            }

            File outFile = new File(outPath);
            if(!outFile.exists()) {
                outFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(outFile);
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != workbook) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
//        try {
//            this.exportExcel("2018","E:\\shiquxianlu\\2018","E:\\vehicleTripOut\\2018");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        new Thread(this).start();
    }
}
