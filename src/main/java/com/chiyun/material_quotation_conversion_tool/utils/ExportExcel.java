package com.chiyun.material_quotation_conversion_tool.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportExcel {
    //
    public static void exportExcel(String fileName, String title, String[] headers, List<Map<String, Object>> dataset, String pattern, HttpServletResponse response) throws IOException {
        boolean flag = false;
        Workbook workbook = null;
        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            try {
                throw new Exception("invalid file name, should be xls or xlsx");
            } catch (Exception e) {
                System.out.print("必须是xls或者xlsx结尾的文件.");
                e.printStackTrace();
            }

        }

        Sheet sheet = workbook.createSheet(title);
        CellStyle style = workbook.createCellStyle();
        // 列名
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            sheet.setColumnWidth(i, 5000);
            //style.setAlignment(CellStyle.ALIGN_CENTER);
            cell.setCellValue(headers[i]);
        }

        Iterator<Map<String, Object>> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);

            Map map = it.next();
            System.out.print(map.toString());
            Set<String> mapKey = (Set<String>) map.keySet();
            System.out.print(mapKey.toString());
            Iterator<String> iterator = mapKey.iterator();
            System.out.print(iterator.toString());
            int num = 0;
            while (iterator.hasNext()) {
                Cell cell = row.createCell(num);
                num++;
                String key = iterator.next();
                System.out.print(key);
                Object obj = map.get(key);
                if (obj instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    cell.setCellValue(sdf.format(obj));
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                } else {
                    if(obj==null){
                        cell.setCellValue(String.valueOf(obj));
                    }else{
                    cell.setCellValue(obj.toString());
                    }
                }
            }
        }
        FileOutputStream fos;
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath()+"//src//main//webapp";
        try {
           // fos = new FileOutputStream(courseFile + "//excel//" + fileName);
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            response.setContentType("application/vnd.ms-excel");
            OutputStream toClient = response.getOutputStream();
            workbook.write(toClient);
            toClient.flush();
            toClient.close();
            flag = true;
        } catch (FileNotFoundException e) {
            System.out.print("文件不存在");
            flag = false;
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("文件写入错误");
            flag = false;
            e.printStackTrace();

        }
    }
}
