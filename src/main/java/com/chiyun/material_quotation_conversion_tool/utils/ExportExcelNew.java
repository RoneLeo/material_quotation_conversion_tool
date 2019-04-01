package com.chiyun.material_quotation_conversion_tool.utils;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportExcelNew {
    public static void exportEXL(ProjectEntity projectEntity, String fileName, List<ExcelDataEntity> dataset, HttpServletResponse response, BigDecimal index) {
        try {
            String dateType = "yyyy";
            // 创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFRow row = null;
            HSSFCell cell = null;

            // 建立新的sheet对象（excel的表单） 并设置sheet名字
            HSSFSheet sheet = wb.createSheet("报价");
            sheet.setDefaultRowHeightInPoints(30);// 设置缺省列高sheet.setDefaultColumnWidth(20);//设置缺省列宽
            int[] width = {256 * 5, 256 * 25, 256 * 20, 256 * 5, 256 * 6, 256 * 12, 256 * 13};
            for (int i = 0; i < 7; i++) {
                sheet.setColumnWidth(i, width[i]);
            }
            //----------------标题样式---------------------
            HSSFCellStyle titleStyle = wb.createCellStyle();        //标题样式
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            Font ztFont = wb.createFont();
            ztFont.setItalic(false);                     // 设置字体为斜体字
            ztFont.setColor(Font.COLOR_NORMAL);            // 将字体设置为“红色”
            ztFont.setFontHeightInPoints((short) 16);    // 将字体大小设置为18px
            ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上
            ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
//             ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）
//              ztFont.setStrikeout(true);                  // 是否添加删除线
            titleStyle.setFont(ztFont);
            //-------------------------------------------
            //----------------二级标题格样式----------------------------------
            HSSFCellStyle titleStyle2 = wb.createCellStyle();        //表格样式
            titleStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titleStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            Font ztFont2 = wb.createFont();
            ztFont2.setItalic(false);                     // 设置字体为斜体字
            ztFont2.setColor(Font.COLOR_NORMAL);            // 将字体设置为“红色”
            ztFont2.setFontHeightInPoints((short) 11);    // 将字体大小设置为18px
            ztFont2.setFontName("宋体");             // 字体应用到当前单元格上
            ztFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
//                 ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）
//                  ztFont.setStrikeout(true);                  // 是否添加删除线
            titleStyle2.setFont(ztFont2);
            //----------------------------------------------------------
            //----------------单元格样式----------------------------------
            HSSFCellStyle cellStyle = wb.createCellStyle();        //表格样式
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            Font cellFont = wb.createFont();
            cellFont.setItalic(false);                     // 设置字体为斜体字
            cellFont.setColor(Font.COLOR_NORMAL);            // 将字体设置为“红色”
            cellFont.setFontHeightInPoints((short) 10);    // 将字体大小设置为18px
            cellFont.setFontName("宋体");             // 字体应用到当前单元格上
//            cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cellStyle.setFont(cellFont);
            cellStyle.setWrapText(true);//设置自动换行
            HSSFCellStyle cellStyle2 = wb.createCellStyle();        //表格样式
            cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyle2.setFont(cellFont);
            cellStyle2.setWrapText(true);//设置自动换行
            //----------------------------------------------------------
            // ----------------------创建第一行---------------
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            row = sheet.createRow(0);
            row.setHeight((short) (228 * 2));
            // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            cell = row.createCell(0);
            // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            // 设置单元格内容
            cell.setCellValue(fileName);
            cell.setCellStyle(cellStyle2);
            // ----------------------------------------------


            // ------------------创建表头start---------------------
            row = sheet.createRow(1); // 创建第二行
            row.setHeight((short) (228 * 2));
//            sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
            cell = row.createCell(0);
            cell.setCellValue("序号");
            cell.setCellStyle(cellStyle);

//            sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
            cell = row.createCell(1);
            cell.setCellValue("产品名称");
            cell.setCellStyle(cellStyle);

//            sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
            cell = row.createCell(2);
            cell.setCellValue("型号及规格");
            cell.setCellStyle(cellStyle);

//            sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 5));
            cell = row.createCell(3);
            cell.setCellValue("单位");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("数量");

            cell = row.createCell(5);
            cell.setCellValue("单价（元）");
            cell.setCellStyle(cellStyle);

//            sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 10));
            cell = row.createCell(6);
            cell.setCellValue("小计（元）");
            cell.setCellStyle(cellStyle);
            //-------------------------表头end---------------------
            int i = 0;
//            BigDecimal sum = new BigDecimal(0);
//            double sum = 0;
            for (; i < dataset.size(); i++) {    //向表格插入数据
                List<Object> data = new ArrayList<>();        //将前台传来的数据存入到list中
                ExcelDataEntity entity = dataset.get(i);
                data.add(i + 1);
                data.add(entity.getHwmc());
                data.add(entity.getXhgg());
                data.add(entity.getHwdw());
                data.add(entity.getSl());
                data.add(entity.getDj().multiply(index).setScale(2, BigDecimal.ROUND_DOWN));
                data.add(index.multiply(entity.getZj()).setScale(2, BigDecimal.ROUND_DOWN));
//                sum = index.multiply(entity.getZj()).add(sum).setScale(2, BigDecimal.ROUND_DOWN);
                int rowNum = 2 + i;    //从第三行开始
                row = sheet.createRow(rowNum);
                row.setHeight((short) (228 * 2));
                for (int j = 0; j < data.size(); j++) {        //将数据添加到单元格中
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, j, j));
                    cell = row.createCell(j);
                    if (j == 0 || j == 4 || j == 5) {
                        cell.setCellValue(Double.valueOf(data.get(j) + ""));

                    } else if (j == 6) {
                        cell.setCellFormula("E" + (rowNum + 1) + "*" + "F" + (rowNum + 1));
                        cell.setCellType(Cell.CELL_TYPE_FORMULA);
                    } else {
                        cell.setCellValue("" + data.get(j) + "");
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
            row = sheet.createRow(2 + i);
            row.setHeight((short) (228 * 2));
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(2 + i, 2 + i, 1, 5));
            cell = row.createCell(1);
            cell.setCellValue("合计");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellFormula("sum(G3:G" + (2 + i) + ")");
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(3 + i);
            row.setHeight((short) (228 * 2));
            cell = row.createCell(0);
            cell.setCellValue(i + 2);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(3 + i, 3 + i, 1, 5));
            cell = row.createCell(1);
            cell.setCellValue("运输费");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellValue(Double.valueOf(String.valueOf(projectEntity.getYsf().setScale(2, BigDecimal.ROUND_DOWN))));
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(4 + i);
            row.setHeight((short) (228 * 2));
            cell = row.createCell(0);
            cell.setCellValue(i + 3);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(4 + i, 4 + i, 1, 5));
            cell = row.createCell(1);
            cell.setCellValue("总价");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
//            cell.setCellValue(String.valueOf(sum.add(projectEntity.getYsf()).setScale(2, BigDecimal.ROUND_DOWN)));
            cell.setCellFormula("sum(G" + (3 + i) + ":G" + (4 + i) + ")");
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(5 + i);
            row.setHeight((short) (228 * 4));
            cell = row.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(5 + i, 5 + i, 0, 6));
            // 设置单元格内容
            cell.setCellValue(projectEntity.getBz());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(6 + i);
            row.setHeight((short) (228 * 2));
            cell = row.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(6 + i, 6 + i, 0, 6));
            // 设置单元格内容
            cell.setCellValue(projectEntity.getBjdw());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle);
            // 输出Excel文件
            fileName = fileName + ".xls";
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            response.setContentType("application/vnd.ms-excel");
            OutputStream toClient = response.getOutputStream();
            wb.write(toClient);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
