package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntityPK;
import com.chiyun.material_quotation_conversion_tool.repository.MaterialDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProMatePriceRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @MustLogin(rolerequired = {1})
    public ApiResult fileImport(@RequestParam @ApiParam("数据文件") MultipartFile file, @RequestParam @ApiParam("省份id") Integer sfid) {

        return ApiResult.SUCCESS();
    }

    @RequestMapping("/findAllBySfid")
    @ApiOperation("根据省份id查询价目信息")
    @MustLogin(rolerequired = {0})
    public ApiResult findAllBySfid(@RequestParam(required = false) @ApiParam("省份id") Integer sfid) {
        if (sfid == null || sfid == 0)
            sfid = SessionHelper.getuser().getSfid();
        return ApiResult.SUCCESS(proMatePriceRepository.findAllInfoBySfid(sfid));
    }

    @RequestMapping("/updateBySfid")
    @ApiOperation("修改省份中某个材料的价目")
    @MustLogin(rolerequired = {1})
    public ApiResult updateBySfid(ProMatePriceEntity proMatePriceEntity) {
        ProMatePriceEntityPK pk = new ProMatePriceEntityPK();
        pk.setSfid(proMatePriceEntity.getSfid());
        pk.setClid(proMatePriceEntity.getClid());
        if (!proMatePriceRepository.existsById(pk))
            return ApiResult.FAILURE("不存在该价目");
        try {
            proMatePriceRepository.save(proMatePriceEntity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE("修改失败");
        }
    }

    @RequestMapping("/deleteBySfid")
    @ApiOperation("根据省份id删除价目信息")
    @MustLogin(rolerequired = {1})
    public ApiResult deleteBySfid(@RequestParam @ApiParam("省份id") Integer sfid) {
        if (!proMatePriceRepository.existsBySfid(sfid))
            return ApiResult.FAILURE("没有该省份的价目信息");
        int num = proMatePriceRepository.deleteAllBySfid(sfid);
        if (num <= 0)
            return ApiResult.FAILURE("删除失败");
        return ApiResult.SUCCESS("删除价目数据 " + num + " 条");
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
