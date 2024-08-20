package com.TrollMarket.TrollMarket;

import com.TrollMarket.TrollMarket.dtos.order.OrderRowDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderToDatabaseDto;
import com.TrollMarket.TrollMarket.models.Order;
import com.TrollMarket.TrollMarket.models.Shipment;
import com.TrollMarket.TrollMarket.repositories.BuyerRepository;
import com.TrollMarket.TrollMarket.repositories.ProductRepository;
import com.TrollMarket.TrollMarket.repositories.SellerRepository;
import com.TrollMarket.TrollMarket.repositories.ShipmentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.bridge.MessageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class ExcelUtility {
    public  static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static final String[] HEADERs={"Tanggal Order","Seller","Buyer","Product","Quantity","Shipment","Total Price"};
    static final   String SHEET="History";
    static BuyerRepository buyerRepository;
    static SellerRepository sellerRepository;
    static ProductRepository productRepository;
    static ShipmentRepository shipmentRepository;
    private static final Logger logger = Logger.getLogger(ExcelUtility.class.getName());


    public static boolean hasExcelFormat(MultipartFile file){
//        if(!TYPE.equals(file.getContentType())){
//            return  false;
//        }
//        return true;
        return TYPE.equals(file.getContentType());
    }
    public static List<OrderToDatabaseDto> excelToList(InputStream is){
        try( Workbook workbook = new XSSFWorkbook(is)){
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<OrderToDatabaseDto> orderList = new ArrayList<OrderToDatabaseDto>();
            int rowNumber= 0;
            while (rows.hasNext()){
                Row currentRow = rows.next();
                //skip header
                if(rowNumber == 0){
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                OrderToDatabaseDto order = new OrderToDatabaseDto();
                int cellIdx = 0;
                while (cellsInRow.hasNext()){
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx){
                        case 0:
                            if (currentCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(currentCell)) {
                                order.setDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            } else if(currentCell.getCellType() == CellType.STRING) {
                                String datestr = currentCell.getStringCellValue();
                                String[] parts = datestr.split("-");
                                try {

                                    if (parts.length == 3) {
                                        int year = Integer.parseInt(parts[0]);
                                        int month = Integer.parseInt(parts[1]);
                                        int day = Integer.parseInt(parts[2]);

                                        LocalDate date = LocalDate.of(year, month, day);
                                        order.setDate(date);
                                    } else {
                                        logger.log(Level.SEVERE, "Invalid date format at row {0}: {1}",
                                                new Object[]{rowNumber, datestr});
                                    }
                                } catch (Exception e) {
                                    logger.log(Level.SEVERE, "Failed to parse date from string at row {0}: {1}",
                                            new Object[]{rowNumber, currentCell.getStringCellValue()});
                                }
                            }else {
                                logger.log(Level.SEVERE, "Unexpected cell type for date at row {0}: {1}",
                                        new Object[]{rowNumber, currentCell.getCellType()});
                            }
                           // order.setDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            break;
                        case 1:
                            break;
                        case 2:
                            order.setBuyer(currentCell.getStringCellValue());
                            break;
                        case 3:
                            order.setProduct(currentCell.getStringCellValue());
                            break;
                        case 4:
                            order.setQuantity((int) currentCell.getNumericCellValue());
                            break;
                        case 5:
                            order.setShipment(currentCell.getStringCellValue());
                            break;
                        case 6:
                            order.setFreight((Double) currentCell.getNumericCellValue());
                            break;
                        case 7:
                            int quantity = order.getQuantity();
                            Double freight = order.getFreight();
                            Double unitPrice = (currentCell.getNumericCellValue() - freight)/quantity;
                            order.setUnitPrice(unitPrice);
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                orderList.add(order);
            }
            workbook.close();
            return orderList;

        } catch (IOException e) {
            throw new RuntimeException("Fail to parse excel file:"+e.getMessage());
        }
    }
}
