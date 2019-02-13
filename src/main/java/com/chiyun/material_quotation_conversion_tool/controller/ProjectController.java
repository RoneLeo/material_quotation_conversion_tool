package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import com.chiyun.material_quotation_conversion_tool.repository.ProjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "添加项目")
    @RequestMapping("add")
    public ApiResult<Object> add(ProjectEntity projectEntity){
        ProjectEntity save = projectRepository.save(projectEntity);
        if(save==null){
            return ApiResult.FAILURE("添加失败");
        }
        return ApiResult.SUCCESS(save);
    }

    @ApiOperation(value = "删除项目")
    @RequestMapping("del")
    public ApiResult<Object> del(Integer id){
        int result = projectRepository.deleteById(id);
        if (result != 1) {
            return ApiResult.FAILURE("删除失败");
        }
        return ApiResult.SUCCESS("删除成功");
    }

    @ApiOperation(value = "修改项目")
    @RequestMapping("update")
    public ApiResult<Object> update(ProjectEntity projectEntity){
        ProjectEntity isExist=projectRepository.findById(projectEntity.getId());
        if (isExist == null){
            return ApiResult.FAILURE("该项目不存在");
        }
        ProjectEntity result = projectRepository.save(projectEntity);
        if(result==null){
            return ApiResult.FAILURE("修改失败");
        }
        return ApiResult.SUCCESS("修改成功");
    }

    @ApiOperation(value = "查詢项目")
    @RequestMapping("findAll")
    public ApiResult<Object> findAll(){
        List<ProjectEntity> result = projectRepository.findAll();
        return ApiResult.SUCCESS(result);
    }

    @ApiOperation(value = "通过id查詢项目")
    @RequestMapping("findById")
    public ApiResult<Object> findById(Integer id){
        ProjectEntity result = projectRepository.findById(id);
        if(result==null){
            return ApiResult.FAILURE("该项目不存在");
        }
        return ApiResult.SUCCESS(result);
    }

    public ProjectEntity add1(ProjectEntity projectEntity){
        ProjectEntity save = projectRepository.save(projectEntity);
        return save;
    }
}
