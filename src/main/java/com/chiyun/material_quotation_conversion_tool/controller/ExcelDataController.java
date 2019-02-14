package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ExcelDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProjectRepository;
import com.chiyun.material_quotation_conversion_tool.utils.ExcelImportUtils;
import com.chiyun.material_quotation_conversion_tool.utils.ExportExcel;
import com.chiyun.material_quotation_conversion_tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    @ApiOperation(value = "导入excel数据")
    @RequestMapping("importExcel")
    public ApiResult<Object> importExcel(String xmmc, @RequestParam(value = "file") MultipartFile file){
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
        ProjectEntity projectEntity=new ProjectEntity();
        if(xmmc.isEmpty()){
            projectEntity.setXmmc(fileName);
        }else{
            projectEntity.setXmmc(xmmc);
        }
        ProjectEntity projectEntity1 = projectController.add1(projectEntity);
        if(projectEntity1==null){
            return ApiResult.FAILURE("添加项目失败");
        }
        //批量导入
        ApiResult message = ExcelImportUtils.batchImport(projectEntity1.getId(),fileName, file, this);
        return message;
    }

    @ApiOperation(value = "折算后导出excel数据")
    @RequestMapping("exportExcel")
    public ApiResult<Object> exportExcel(Integer xmbh, BigDecimal discount, HttpServletResponse response){
        List<Map<String, Object>> list = new ArrayList<>();
        List<ExcelDataEntity> excelDataEntity=excelDataRepository.findByXmbh(xmbh);
        ProjectEntity projectEntity =projectRepository.findById(xmbh);
        if(excelDataEntity.isEmpty()){
            return ApiResult.FAILURE("不存在该项目的数据");
        }
        BigDecimal hjjg = null;
        for(ExcelDataEntity entity:excelDataEntity){
            Map<String, Object> map=new LinkedHashMap<>();
            map.put("产品名称",entity.getHwmc());
            map.put("型号及规格",entity.getXhgg());
            map.put("单位",entity.getHwdw());
            map.put("数量",entity.getSl());
            map.put("单价（元）",entity.getDj().multiply(discount));
            map.put("小计（元）",entity.getZj().multiply(discount));
            list.add(map);
            if (hjjg==null){
                hjjg=entity.getZj().multiply(discount);
            }else{
                hjjg=entity.getZj().multiply(discount).add(hjjg);
            }
        }
            Map<String, Object> map1 = new LinkedHashMap<>();
            map1.put("产品名称", "");
            map1.put("型号及规格", "");
            map1.put("单位", "");
            map1.put("数量", "");
            map1.put("单价（元）", "合计");
            map1.put("小计（元）", hjjg);
            list.add(map1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String pattern = sdf.format(new Date());
        String fileName = null;
        String[] headers = list.get(0).keySet().toArray(new String[0]);
        String title = "报价";
        fileName = projectEntity.getXmmc() + ".xlsx";
        try {
            ExportExcel.exportExcel(fileName, title, headers, list, pattern, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "折算后的总价")
    @RequestMapping("discount")
    public ApiResult<Object> discount(Integer xmbh, BigDecimal discount) {
        List<ExcelDataEntity> excelDataEntity=excelDataRepository.findByXmbh(xmbh);
        if(excelDataEntity.isEmpty()){
            return ApiResult.FAILURE("不存在该项目的数据");
        }
        BigDecimal hjjg = null;
        for(int i=0;i<excelDataEntity.size();i++){
            if (hjjg==null){
                hjjg=excelDataEntity.get(i).getZj().multiply(discount);
                //System.out.print("第一次:"+hjjg);
            }else{
            hjjg=excelDataEntity.get(i).getZj().multiply(discount).add(hjjg);
                //System.out.print("2222222："+hjjg);
            }
        }
        return ApiResult.SUCCESS(hjjg);
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
