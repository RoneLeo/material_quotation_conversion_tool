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

@Api(description = "项目数据")
@RequestMapping(value = "/projectdata", method = {RequestMethod.GET, RequestMethod.POST})
@RestController
public class ExcelDataController {
    @Resource
    private ExcelDataRepository excelDataRepository;
    @Resource
    private ProjectRepository projectRepository;


    @ApiOperation(value = "通过Excel导入项目材料数据")
    @RequestMapping("/importExcel")
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
        //批量导入
        ApiResult message = ExcelImportUtils.batchImport(xmmc, fileName, file, this, userEntity);
        return message;
    }

    @ApiOperation(value = "查询项目已有材料")
    @RequestMapping("findAllByXmbh")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> findAllByXmbh(@RequestParam @ApiParam("项目id") int xmbh) {
        List<Map<String, Object>> list = excelDataRepository.findAllByXmbhAndSfid(xmbh);
        return ApiResult.SUCCESS(list);
    }

    @ApiOperation(value = "项目基价、成本价、折算价")
    @RequestMapping("/discount")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> discount(@RequestParam @ApiParam("项目id") int xmbh,
                                      @RequestParam @ApiParam("折算比率") BigDecimal discount) {
        if (discount == null)
            discount = new BigDecimal(1);
        BigDecimal zsj = new BigDecimal(0);
        BigDecimal jj = new BigDecimal(0);
        BigDecimal cbj = new BigDecimal(0);
        List<Map<String, Object>> list = excelDataRepository.findAllByXmbhAndSfid(xmbh);
        for (Map<String, Object> map : list) {
            BigDecimal sl = BigDecimal.valueOf((Integer) map.get("clsl"));
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

    @ApiOperation(value = "给项目添加一个材料")
    @RequestMapping("addMaterial")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> addMaterial(@RequestParam(required = false) @ApiParam("项目id") Integer xmbh,
                                         @RequestParam(required = false) @ApiParam("材料名称") String clmc,
                                         @RequestParam(required = false) @ApiParam("材料规格") String clgg,
                                         @RequestParam(required = false) @ApiParam("材料单位") String cldw,
                                         @RequestParam(required = false) @ApiParam("材料数量") Integer clsl) {
        UserEntity userEntity = SessionHelper.getuser();
        if (userEntity.getJs() == 1)
            return ApiResult.FAILURE("管理员无需添加项目材料");
        ProjectEntity projectEntity = projectRepository.findById(xmbh);
        if (projectEntity == null) {
            return ApiResult.FAILURE("没有该项目信息");
        }
        if (excelDataRepository.existsByXmbhAndClgg(projectEntity.getId(), StringUtils.isEmpty(clgg) ? "" : clgg))
            return ApiResult.FAILURE("该项目已存在该规格型号的材料");
        ExcelDataEntity excelDataEntity = new ExcelDataEntity();
        excelDataEntity.setXmbh(projectEntity.getId());
        excelDataEntity.setClgg(StringUtils.isEmpty(clgg) ? "" : clgg);
        excelDataEntity.setClmc(StringUtils.isEmpty(clmc) ? "" : clmc);
        excelDataEntity.setCldw(StringUtils.isEmpty(cldw) ? "" : cldw);
        excelDataEntity.setClsl(clsl);
        return doSave(excelDataEntity);
    }

    public ApiResult<Object> doSave(ExcelDataEntity excelDataEntity) {
        try {
            excelDataRepository.save(excelDataEntity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.FAILURE();
        }
    }


    @ApiOperation("导出所选价表")
    @RequestMapping("/getexcelnew")
    @MustLogin(rolerequired = {0})
    public void getexcelnew(@RequestParam @ApiParam("项目id") int xmbh,
                            @RequestParam @ApiParam("折算比率") BigDecimal discount,
                            @RequestParam @ApiParam("导出类型:0-折算价表，1-基价表，2-成本价表") int lx, HttpServletResponse response) throws IOException {
        ProjectEntity projectEntity = projectRepository.findById(xmbh);
        if (projectEntity == null) {
            MessageUtils.resultMsg(response, ApiResult.FAILURE("没有该项目信息"));
            return;
        }
        List<Map<String, Object>> list = excelDataRepository.findAllByXmbhAndSfid(projectEntity.getId());
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
