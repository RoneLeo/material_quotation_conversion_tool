package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiPageResult;
import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.repository.MaterialDataRepository;
import com.chiyun.material_quotation_conversion_tool.utils.MaterialImportUtils;
import com.chiyun.material_quotation_conversion_tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @ApiOperation("获取所有材料价目")
    @RequestMapping("/findAll")
    @MustLogin(rolerequired = {0})
    public ApiResult findAll(@RequestParam(required = false) @ApiParam("用户id，仅对管理员有效") String uid) {
        UserEntity userEntity = SessionHelper.getuser();
        List<MaterialdataEntity> list;
        if (userEntity.getJs() != 1) {
            list = materialDataRepository.findAllByUid(userEntity.getId());
        } else {
            list = materialDataRepository.findAllByUid(uid);
        }
        return ApiResult.SUCCESS(list);
    }

    @ApiOperation("获取所有材料价目")
    @RequestMapping("/findAllByPage")
    @MustLogin(rolerequired = {0})
    public ApiResult findAllByPage(@RequestParam(required = false) @ApiParam("用户id，仅对管理员有效") String uid,
                                   @RequestParam @ApiParam("页码") int page,
                                   @RequestParam @ApiParam("分页大小") int pagesize) {
        UserEntity userEntity = SessionHelper.getuser();
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        Page<MaterialdataEntity> list;
        if (userEntity.getJs() != 1) {
            list = materialDataRepository.findAllByUid(userEntity.getId(), pageable);
        } else {
            list = materialDataRepository.findAllByUid(uid, pageable);
        }
        return ApiPageResult.SUCCESS(list.getContent(), page, pagesize, list.getTotalElements(), list.getTotalPages());
    }

    @ApiOperation("修改一个材料价目")
    @RequestMapping("/updateOne")
    @MustLogin(rolerequired = {0})
    public ApiResult updateOne(@RequestParam @ApiParam("材料id") Integer clid,
                               @RequestParam(required = false) @ApiParam("基价") BigDecimal jj,
                               @RequestParam(required = false) @ApiParam("成本价") BigDecimal cbj) {
        Optional<MaterialdataEntity> optional = materialDataRepository.findById(clid);
        if (!optional.isPresent())
            return ApiResult.FAILURE("不存在该材料价目");
        MaterialdataEntity entity = optional.get();
        entity.setJj(jj == null ? new BigDecimal(0) : jj);
        entity.setCbj(cbj == null ? new BigDecimal(0) : cbj);
        try {
            materialDataRepository.save(entity);
            return ApiResult.SUCCESS(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.FAILURE("修改材料价目失败");
        }
    }

    @ApiOperation("添加一个材料价目")
    @RequestMapping("/addOne")
    @MustLogin(rolerequired = {0})
    public ApiResult addOne(@RequestParam(required = false) @ApiParam("材料名称") String clmc,
                            @RequestParam(required = false) @ApiParam("材料规格") String clgg,
                            @RequestParam(required = false) @ApiParam("材料单位") String cldw,
                            @RequestParam(required = false) @ApiParam("基价") BigDecimal jj,
                            @RequestParam(required = false) @ApiParam("成本价") BigDecimal cbj) {
        UserEntity userEntity = SessionHelper.getuser();
        if (materialDataRepository.existsByUidAndClgg(userEntity.getId(), clgg)) {
            return ApiResult.FAILURE("已存在该规格材料价目");
        }
        MaterialdataEntity entity = new MaterialdataEntity();
        entity.setClmc(StringUtils.isEmpty(clmc) ? "" : clmc);
        entity.setClgg(StringUtils.isEmpty(clgg) ? "" : clgg);
        entity.setCldw(StringUtils.isEmpty(cldw) ? "" : cldw);
        entity.setJj(jj == null ? new BigDecimal(0) : jj);
        entity.setCbj(cbj == null ? new BigDecimal(0) : cbj);
        entity.setUid(userEntity.getId());
        try {
            materialDataRepository.save(entity);
            return ApiResult.SUCCESS(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.FAILURE("添加材料价目失败");
        }
    }

    @ApiOperation("用户导入可用材料价目")
    @RequestMapping("/fileImport")
    @MustLogin(rolerequired = {0})
    public ApiResult fileImport(@RequestParam @ApiParam("数据文件") MultipartFile file) {
        UserEntity userEntity = SessionHelper.getuser();
        if (userEntity.getJs() == 1) {
            return ApiResult.FAILURE("管理员无需导入材料");
        }
        //判断文件是否为空
        if (file == null) {
            return ApiResult.FAILURE("文件不能为空");
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //验证文件名是否合格
        if (!MaterialImportUtils.validateExcel(fileName)) {
            return ApiResult.FAILURE("文件必须是excel格式");
        }
        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            return ApiResult.FAILURE("文件不能为空");
        }
        //批量导入
        ApiResult message = MaterialImportUtils.batchImport(fileName, file, this, userEntity);
        return ApiResult.SUCCESS(message);
    }

    @ApiOperation("删除所选材料价目")
    @RequestMapping("/deleteOneById")
    @MustLogin(rolerequired = {0})
    public ApiResult deleteOneById(@RequestParam() @ApiParam("材料id") int clid) {
        UserEntity userEntity = SessionHelper.getuser();
        int sum = materialDataRepository.deleteByUidAndClid(userEntity.getId(), clid);
        if (sum == 1)
            return ApiResult.SUCCESS(sum);
        return ApiResult.FAILURE("删除失败");
    }

    @ApiOperation("删除选中用户所有材料价目")
    @RequestMapping("/deleteAllByUid")
    @MustLogin(rolerequired = {0})
    public ApiResult deleteAllByUid(@RequestParam(required = false) @ApiParam("用户id，仅对管理员用户有效") String uid) {
        UserEntity userEntity = SessionHelper.getuser();
        int sum = 0;
        if (userEntity.getJs() == 1) {
            if (StringUtils.isEmpty(uid))
                return ApiResult.FAILURE("用户id不能为空");
            sum = materialDataRepository.deleteAllByUid(uid);
        } else {
            sum = materialDataRepository.deleteAllByUid(userEntity.getId());
        }
        if (sum <= 0) {
            return ApiResult.FAILURE("删除失败");
        }
        return ApiResult.SUCCESS(sum);
    }


    public ApiResult addmate(MaterialdataEntity entity) {
        try {
            materialDataRepository.save(entity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE("已存在该规格型号的材料");
        }
    }

}
