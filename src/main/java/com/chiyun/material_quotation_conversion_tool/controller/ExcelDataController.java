package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ExcelDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProjectRepository;
import com.chiyun.material_quotation_conversion_tool.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "折算工具")
@RequestMapping(value = "/conbersiontool", method = {RequestMethod.GET, RequestMethod.POST})
@RestController
public class ExcelDataController {
    @Resource
    private ExcelDataRepository excelDataRepository;
    @Resource
    private ProjectController projectController;
    @Resource
    private ProjectRepository projectRepository;
    @Resource
    private ProMatePriceController proMatePriceController;


    @ApiOperation(value = "导入excel数据")
    @RequestMapping("importExcel")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> importExcel(String xmmc, @RequestParam(value = "file") MultipartFile file) {
        UserEntity userEntity = SessionHelper.getuser();
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
        if (xmmc.isEmpty()) {
            xmmc = fileName;
        }
//        ProjectEntity projectEntity=new ProjectEntity();
//        if(xmmc.isEmpty()){
//            projectEntity.setXmmc(fileName);
//        }else{
//            projectEntity.setXmmc(xmmc);
//        }
//        ProjectEntity projectEntity1 = projectController.add1(projectEntity);
//        if(projectEntity1==null){
//            return ApiResult.FAILURE("添加项目失败");
//        }
        //批量导入
        ApiResult message = ExcelImportUtils.batchImport(xmmc, fileName, file, this, userEntity);
        return message;
    }


    @ApiOperation(value = "折算后的总价")
    @RequestMapping("discount")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> discount(@RequestParam @ApiParam("项目id") int xmbh,
                                      @RequestParam @ApiParam("折算比率") BigDecimal discount) {
        UserEntity userEntity = SessionHelper.getuser();
//        if (excelDataEntity.isEmpty()) {
//            return ApiResult.FAILURE("不存在该项目的数据");
//        }
        if (discount == null)
            discount = new BigDecimal(1);
        BigDecimal zsj = new BigDecimal(0);
        BigDecimal jj = new BigDecimal(0);
        BigDecimal cbj = new BigDecimal(0);
//        for (ExcelDataEntity anExcelDataEntity : excelDataEntity) {
//            hjjg = (anExcelDataEntity.getDj().multiply(discount).multiply(BigDecimal.valueOf(anExcelDataEntity.getSl()))).add(hjjg);
//        }
        List<Map<String, Object>> list = excelDataRepository.findAllByXmbhAndSfid(xmbh, userEntity.getSfid());
        for (Map<String, Object> map : list) {
            BigDecimal sl = BigDecimal.valueOf((Integer) map.get("sl"));
            jj = ((BigDecimal) map.get("jj")).multiply(sl).add(jj);
            cbj = ((BigDecimal) map.get("cbj")).multiply(sl).add(cbj);
            zsj = ((BigDecimal) map.get("jj")).multiply(sl).multiply(discount).add(zsj);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jj", jj);
        map.put("cbj", cbj);
        map.put("zsj", zsj);
        return ApiResult.SUCCESS(map);
    }

    @ApiOperation(value = "保存数据")
    @RequestMapping("doSave")
    public ApiResult<Object> doSave(ExcelDataEntity excelDataEntity) {
        try {
            ExcelDataEntity entity = excelDataRepository.save(excelDataEntity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE();
        }
    }


    @ApiOperation("导出所选价表")
    @RequestMapping("/getexcelnew")
    @MustLogin(rolerequired = {0})
    public void getexcelnew(@RequestParam @ApiParam("项目id") int xmbh,
                            @RequestParam @ApiParam("折算比率") BigDecimal discount,
                            @RequestParam @ApiParam("导出类型:0-折算价表，1-基价表，2-成本价表") int lx, HttpServletResponse response) throws IOException {
        UserEntity userEntity = SessionHelper.getuser();
        ProjectEntity projectEntity = projectRepository.findById(xmbh);
        if (projectEntity == null) {
            MessageUtils.resultMsg(response, ApiResult.FAILURE("没有该项目信息"));
            return;
        }
        List<Map<String, Object>> list = excelDataRepository.findAllByXmbhAndSfid(projectEntity.getId(), userEntity.getSfid());
        if (list.isEmpty()) {
            MessageUtils.resultMsg(response, ApiResult.FAILURE("没有数据"));
            return;
        }
        String title = projectEntity.getXmmc();
        ExportExcelNew.exportEXL(projectEntity, title, list, response, discount, lx);
    }


    public ProjectEntity saveproject(ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }
}
