package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ExcelDataRepository;
import com.chiyun.material_quotation_conversion_tool.utils.ExcelImportUtils;
import com.chiyun.material_quotation_conversion_tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(description = "折算工具")
@RequestMapping(value = "/conbersiontool", method = {RequestMethod.GET, RequestMethod.POST})
@RestController
public class ExcelDataController {
    @Resource
    private ExcelDataRepository excelDataRepository;


    @ApiOperation(value = "折算后导入excel数据")
    @RequestMapping("importExcel")
    public ApiResult<Object> importExcel(Integer xmbh, @RequestParam(value = "file") MultipartFile file,  @RequestParam(value = "折率（小数）")float discount){
        //判断文件是否为空
        if (file == null) {
            return ApiResult.FAILURE("文件不能为空");
        }

        //获取文件名
        String fileName = file.getOriginalFilename();

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            return ApiResult.FAILURE("文件必须是excel格式");
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            return ApiResult.FAILURE("文件不能为空");
        }
        //批量导入
        ApiResult message = ExcelImportUtils.batchImport(xmbh,fileName, file, this, discount);
        return message;
    }

    @ApiOperation(value = "保存数据")
    @RequestMapping("doSave")
    public ApiResult<Object> doSave(ExcelDataEntity excelDataEntity){
        try{
            ExcelDataEntity entity = excelDataRepository.save(excelDataEntity);
            return ApiResult.SUCCESS();
        }catch (Exception e){
            return ApiResult.FAILURE();
        }
    }
}
