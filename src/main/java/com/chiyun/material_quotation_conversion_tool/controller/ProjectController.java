package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiPageResult;
import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ExcelDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProjectRepository;
import com.chiyun.material_quotation_conversion_tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Api(description = "项目管理")
@RequestMapping(value = "/project", method = {RequestMethod.GET, RequestMethod.POST})
@RestController
public class ProjectController {

    @Resource
    private ProjectRepository projectRepository;
    @Resource
    private ExcelDataRepository excelDataRepository;

    @ApiOperation(value = "添加项目")
    @RequestMapping("add")
    @MustLogin(rolerequired = {0})
    @Deprecated
    public ApiResult<Object> add(@RequestParam(required = false) @ApiParam("外在项目名称") String wzxmmc,
                                 @RequestParam(required = false) @ApiParam("项目名称") String xmmc,
                                 @RequestParam(required = false) @ApiParam("备注") String bz,
                                 @RequestParam(required = false) @ApiParam("报价单位") String bjdw,
                                 @RequestParam(required = false) @ApiParam("运输费") BigDecimal ysf,
                                 @RequestParam(required = false) @ApiParam("第三方检测费") BigDecimal jcf) {
        UserEntity userEntity = SessionHelper.getuser();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setUid(userEntity.getId());
        projectEntity.setYsf(projectEntity.getYsf() == null ? new BigDecimal(0) : projectEntity.getYsf());
        try {
            projectRepository.save(projectEntity);
            return ApiResult.SUCCESS(projectEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.FAILURE("添加失败");
        }
    }

    @ApiOperation(value = "删除项目")
    @RequestMapping("/del")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> del(Integer id) {
        ProjectEntity projectEntity = projectRepository.findById(id);
        if (projectEntity == null) {
            return ApiResult.FAILURE("该项目不存在");
        }
        UserEntity userEntity = SessionHelper.getuser();
        if (!userEntity.getId().equals(projectEntity.getUid()) && userEntity.getJs() != 1) {
            return ApiResult.FAILURE("没有删除权限");
        } else {
            try {
                projectRepository.delete(projectEntity);
                return ApiResult.SUCCESS();
            } catch (Exception e) {
                return ApiResult.FAILURE("删除失败");
            }
        }
    }

    @ApiOperation(value = "修改项目")
    @RequestMapping("update")
    public ApiResult<Object> update(ProjectEntity projectEntity) {
        ProjectEntity isExist = projectRepository.findById(projectEntity.getId());
        if (isExist == null) {
            return ApiResult.FAILURE("该项目不存在");
        }
        if (StringUtils.isNotEmpty(projectEntity.getXmmc()))
            isExist.setXmmc(projectEntity.getXmmc());
        if (StringUtils.isNotEmpty(projectEntity.getWzxmmc()))
            isExist.setWzxmmc(projectEntity.getWzxmmc());
        if (StringUtils.isNotEmpty(projectEntity.getBjdw()))
            isExist.setBjdw(projectEntity.getBjdw());
        if (StringUtils.isNotEmpty(projectEntity.getBz()))
            isExist.setBz(projectEntity.getBz());
        if (projectEntity.getYsf() != null)
            isExist.setYsf(projectEntity.getYsf());
        if (projectEntity.getJcf() != null)
            isExist.setJcf(projectEntity.getJcf());
        try {
            projectRepository.save(isExist);
            return ApiResult.SUCCESS("修改成功");
        } catch (Exception e) {
            return ApiResult.FAILURE("修改失败");
        }
    }

    @ApiOperation(value = "查詢项目")
    @RequestMapping("findAll")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> findAll() {
        UserEntity userEntity = SessionHelper.getuser();
        List<ProjectEntity> result;
        if (userEntity.getJs() == 1) {
            result = projectRepository.findAll();
        } else {
            result = projectRepository.findAllByUid(userEntity.getId());
        }
        return ApiResult.SUCCESS(result);
    }

    @ApiOperation(value = "分页获取项目")
    @RequestMapping("findAllByPage")
    @MustLogin(rolerequired = {0})
    public ApiResult<Object> findAllByPage(@RequestParam @ApiParam("页码") int page, @RequestParam @ApiParam("分页大小") int pagesize) {
        UserEntity userEntity = SessionHelper.getuser();
        Page<ProjectEntity> result;
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        if (userEntity.getJs() == 1) {
            result = projectRepository.findAll(pageable);
        } else {
            result = projectRepository.findAllByUid(userEntity.getId(), pageable);
        }
        return ApiPageResult.SUCCESS(result.getContent(), page, pagesize, result.getTotalElements(), result.getTotalPages());
    }

    @ApiOperation(value = "通过id查詢项目")
    @RequestMapping("findById")
    @Deprecated
    public ApiResult<Object> findById(Integer id) {
        ProjectEntity result = projectRepository.findById(id);
        if (result == null) {
            return ApiResult.FAILURE("该项目不存在");
        }
        return ApiResult.SUCCESS(result);
    }

    public ProjectEntity add1(ProjectEntity projectEntity) {
        ProjectEntity save = projectRepository.save(projectEntity);
        return save;
    }
}
