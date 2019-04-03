package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import com.chiyun.material_quotation_conversion_tool.repository.MaterialDataRepository;
import com.chiyun.material_quotation_conversion_tool.utils.ExcelImportUtils;
import com.chiyun.material_quotation_conversion_tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wazto on 2019/4/1.
 */
@Api(description = "材料管理")
@RestController
@RequestMapping(value = "/material", method = {RequestMethod.POST, RequestMethod.GET})
public class MaterialDataController {
    @Resource
    private MaterialDataRepository materialDataRepository;

    private Map<String, MaterialdataEntity> entityMap = new HashMap<>();

    @ApiOperation("获取所有材料")
    @RequestMapping("/findAll")
    public ApiResult findAll() {
        return ApiResult.SUCCESS(materialDataRepository.findAll());
    }

    @ApiOperation("导入材料")
    @RequestMapping("/fileImport")
    public ApiResult fileImport(MultipartFile file) {
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
//        ApiResult message = ExcelImportUtils.batchImport("", fileName, file, this);
        return ApiResult.SUCCESS();
    }

    public MaterialdataEntity addmate(MaterialdataEntity entity) {
        try {
            materialDataRepository.save(entity);
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    public void setEntityMap() {
        entityMap.clear();
        entityMap = new HashMap<>();
        List<MaterialdataEntity> list = materialDataRepository.findAll();
        for (MaterialdataEntity entity : list) {
            entityMap.put(entity.getGg(), entity);
        }
    }

    public MaterialdataEntity getMateByXh(String xh) {
        if (entityMap == null || entityMap.isEmpty()) {
            setEntityMap();
        }
        if (entityMap.containsKey(xh))
            return entityMap.get(xh);
        return null;
    }
}
