package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntity;
import com.chiyun.material_quotation_conversion_tool.repository.MaterialDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProMatePriceRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wazto on 2019/4/1.
 */
@Api(description = "省份材料管理")
@RestController
@RequestMapping(value = "/promateprice", method = {RequestMethod.POST, RequestMethod.GET})
public class ProMatePriceController {

    @Resource
    private ProMatePriceRepository proMatePriceRepository;
    @Resource
    private MaterialDataRepository materialDataRepository;

    @Resource
    private MaterialDataController materialDataController;

    @RequestMapping("/fileImport")
    @ApiOperation("文件导入某省份数据")
    public ApiResult fileImport(MultipartFile file, Integer sfid) {

        return ApiResult.SUCCESS();
    }

    public boolean add(MaterialdataEntity old, BigDecimal jj, BigDecimal cbj, Integer sfid) {
        MaterialdataEntity ob = materialDataController.getMateByXh(old.getGg());
        if (ob == null)
            return false;
        try {
            materialDataRepository.save(old);
        } catch (Exception e) {
            return false;
        }
        ProMatePriceEntity entity = new ProMatePriceEntity();
        entity.setSfid(sfid);
        entity.setClid(old.getId());
        entity.setCbj(cbj);
        entity.setJj(jj);
        try {
            proMatePriceRepository.save(entity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    Map<Integer, Map<String, Object>> datamap = new HashMap<>();

    public boolean xxxx(Integer sfid) {
        Map<String, Object> targermap = datamap.get(sfid);
        if (targermap == null || targermap.isEmpty()) {
            List<ProMatePriceEntity> list = proMatePriceRepository.findAllBySfid(sfid);
            for (ProMatePriceEntity entity : list) {

            }
        }
        return true;
    }
}
