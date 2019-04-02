package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ExcelDataRepository;
import com.chiyun.material_quotation_conversion_tool.repository.ProjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public ApiResult<Object> add(ProjectEntity projectEntity) {
        UserEntity userEntity = SessionHelper.getuser();
        projectEntity.setUid(userEntity.getId());
        try {
            projectRepository.save(projectEntity);
            return ApiResult.SUCCESS(projectEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.FAILURE("添加失败");
        }
    }

    @ApiOperation(value = "删除项目")
    @RequestMapping("del")
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
        ProjectEntity result = projectRepository.save(projectEntity);
        if (result == null) {
            return ApiResult.FAILURE("修改失败");
        }
        return ApiResult.SUCCESS("修改成功");
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
