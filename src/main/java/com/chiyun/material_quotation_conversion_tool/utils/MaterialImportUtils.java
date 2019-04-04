package com.chiyun.material_quotation_conversion_tool.utils;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.controller.ExcelDataController;
import com.chiyun.material_quotation_conversion_tool.controller.MaterialDataController;
import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaterialImportUtils {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    /**
     * 上传excel文件到临时目录后并开始解析
     *
     * @param mfile
     * @return
     */
    public static ApiResult batchImport(String fileName, MultipartFile mfile, MaterialDataController materialDataController, UserEntity userEntity) {

        File uploadDir = new File("C:\\fileupload");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File("C:\\fileupload\\" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (MaterialImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb, tempFile, materialDataController, userEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        if (r == 0 && c == 0)
            return ApiResult.FAILURE("导入出错！请检查数据格式！");
        return ApiResult.FAILURE("已导入" + sj + "条数据，" + "第" + (r + 1) + "行" + (c + 1) + "列数据有问题，已停止导入，请仔细检查！");
    }

    static int r = 0;
    static int c = 0;

    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @return
     */
    static int sj = 0;

    private static ApiResult readExcelValue(Workbook wb, File tempFile, MaterialDataController materialDataController, UserEntity userEntity) throws ParseException {
        //导入数据数量

        //错误信息接收器
        StringBuilder errorMsg = new StringBuilder();
        //得到第一个shell
        Sheet sheet1 = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet1.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第1行算起
        if (totalRows >= 1 && sheet1.getRow(1) != null) {
            totalCells = sheet1.getRow(1).getPhysicalNumberOfCells();
        }

        String br = "<br/>";

        int sfbz = 0;
        //循环Excel行数,从第三行开始。标题不入库
        for (r = 2; r < totalRows; r++) {
            MaterialdataEntity materialdataEntity = new MaterialdataEntity();
            Row row = sheet1.getRow(r);
            if (row == null || StringUtils.isRowEmpty(row)) {
                errorMsg.append(br).append("第").append(r + 1).append("行数据有问题，请仔细检查！");
                continue;
            }
            //循环Excel的列
            for (c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (c == 0) {
                    materialdataEntity.setClmc(cell.getStringCellValue());
                } else if (c == 1) {
                    materialdataEntity.setClgg(cell.getStringCellValue());
                } else if (c == 2) {
                    materialdataEntity.setCldw(cell.getStringCellValue());
                } else if (c == 3) {
                    materialdataEntity.setJj(BigDecimal.valueOf(cell.getNumericCellValue()));
                } else if (c == 4) {
                    materialdataEntity.setCbj(BigDecimal.valueOf(cell.getNumericCellValue()));
                }
            }
            materialdataEntity.setUid(userEntity.getId());
            ApiResult result = materialDataController.addmate(materialdataEntity);
            if (result.getResCode() == -1) {
                errorMsg.append(br).append("第").append(r + 1).append("行，").append(result.getResMsg());
            } else if (result.getResCode() == 200) {
                sj++;
            }
        }

        //删除上传的临时文件
        if (tempFile.exists()) {
            tempFile.delete();
        }

        errorMsg.insert(0, "导入成功，共" + sj + "条数据！");
        return ApiResult.SUCCESS(errorMsg.toString());
    }
}