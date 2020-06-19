package com.demo.controller;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 * 单元格合并,背景颜色,边框的功能
 */
public class ExcelPoiTest {

    public static void main(String[] args) {

        HSSFWorkbook workbook = new HSSFWorkbook();  // 创建一个excel
        // excel生成过程: excel-->sheet-->row-->cell
        HSSFSheet sheet = workbook.createSheet("test"); // 为excel创建一个名为test的sheet页
        HSSFRow row = sheet.createRow(1); // 创建一行,参数2表示第一行
        HSSFCell cellB2 = row.createCell(1); // 在B2位置创建一个单元格
        HSSFCell cellB3 = row.createCell(2); // 在B3位置创建一个单元格
        cellB2.setCellValue("单元格B2"); // B2单元格填充内容
        cellB3.setCellValue("单元格B3"); // B3单元格填充内容

        HSSFCellStyle headStyle = workbook.createCellStyle(); // 单元格样式
        Font headFontStyle = workbook.createFont(); // 字体样式
        headFontStyle.setBold(true); // 加粗
        headFontStyle.setFontName("黑体"); // 字体
        headFontStyle.setFontHeightInPoints((short) 11); // 大小
        // 将字体样式添加到单元格样式中
        headStyle.setFont(headFontStyle);
        headStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
        headStyle.setBorderBottom(BorderStyle.THIN);// 边框
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//灰色
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle bodyStyle = workbook.createCellStyle(); // 单元格样式
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
        bodyStyle.setBorderBottom(BorderStyle.THIN);// 边框
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);

        cellB2.setCellStyle(headStyle); // 为B2单元格添加样式

        writeHead(sheet,0, 0, 0, 10,"标题2",headStyle);
        writeHead(sheet,1, 1, 0, 10,"标题1",headStyle);
        for (int i = 2; i < 10 ; i++) {
            writeBody(sheet,i, 0,"内容"+i,bodyStyle);
        }

        // 输出到本地
        String excelName = "D:/temp/00/students4.xls";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(excelName);
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            out = null;
        }
    }
    private static void writeBody(HSSFSheet sheet, int yIndex,int xIndex, String cellValue, CellStyle bodyStyle) {
        Row row = sheet.createRow(yIndex);
        Cell cell = row.createCell(xIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(bodyStyle);
    }

    private static void writeHead(HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String cellValue, CellStyle style) {
        // 合并单元格
        CellRangeAddress cra =new CellRangeAddress(firstRow, lastRow, firstCol, lastCol); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);
        Row row = sheet.createRow(firstRow);
        Cell cell = row.createCell(0);
        cell.setCellValue(cellValue);
        setStyle(sheet, cra);
        cell.setCellStyle(style);
    }

    private static void setStyle(HSSFSheet sheet, CellRangeAddress cra) {
        // 使用RegionUtil类为合并后的单元格添加边框
        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet); // 上边框
    }

}
