package com.TrollMarket.TrollMarket;

import com.TrollMarket.TrollMarket.dtos.order.OrderRowDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Font;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
//FILE UNTUK DATABASE KE EXCEL
public class ExcelGenerator {
    private List<OrderRowDto> orderList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List<OrderRowDto> orderList){
        this.orderList =orderList;
        workbook = new XSSFWorkbook();
    }
    private void writeHeader(){
        sheet = workbook.createSheet("History");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row,0,"Tanggal Order",style);
        createCell(row,1,"Selller",style);
        createCell(row,2,"Buyer",style);
        createCell(row,3,"Product",style);
        createCell(row,4,"Quantity",style);
        createCell(row,5,"Shipment",style);
        createCell(row,6,"Price Shipment",style);
        createCell(row,7,"Total Price",style);
    }
    

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(valueOfCell instanceof Integer){
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof  Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof  Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof  String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof LocalDateTime) {
            LocalDate localDate = (LocalDate) valueOfCell;
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
            cell.setCellStyle(createDateStyle());
        } else {
            cell.setCellValue(valueOfCell.toString());
        }
        cell.setCellStyle(style);
    }

    private CellStyle createStyle(boolean isBold, int fontSize) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeightInPoints((short) fontSize); // Gunakan setFontHeightInPoints
        style.setFont(font);
        return style;
    }

    private CellStyle createDateStyle() {
        CellStyle dateStyle = workbook.createCellStyle();
        XSSFFont dateFont = workbook.createFont();
        dateFont.setFontHeightInPoints((short) 14); // Gunakan setFontHeightInPoints
        dateStyle.setFont(dateFont);
        DataFormat poiFormat = workbook.createDataFormat();
        dateStyle.setDataFormat(poiFormat.getFormat("dd-mm-yyyy"));
        return dateStyle;
    }

    private void write(){
        int rowCount = 1;
        CellStyle style = createStyle(false, 14);

        for(OrderRowDto record:orderList){
            Row row =sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row,columnCount++,record.getDate(),style);
            createCell(row,columnCount++,record.getSeller(),style);
            createCell(row,columnCount++,record.getBuyer(),style);
            createCell(row,columnCount++,record.getProduct(),style);
            createCell(row,columnCount++,record.getQuantity(),style);
            createCell(row,columnCount++,record.getShipment(),style);
            createCell(row,columnCount++,record.getShipmentPrice(),style);
            createCell(row,columnCount++,record.getTotalPrice(),style);
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException{
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
