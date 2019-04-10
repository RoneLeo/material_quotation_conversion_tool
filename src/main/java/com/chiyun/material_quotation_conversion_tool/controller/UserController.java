package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.ApiPageResult;
import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import com.chiyun.material_quotation_conversion_tool.common.SessionHelper;
import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.repository.UserRepository;
import com.chiyun.material_quotation_conversion_tool.utils.DateUtils;
import com.chiyun.material_quotation_conversion_tool.utils.MD5Util;
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

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * Created by wazto on 2019/4/2.
 */
@Api(description = "用户管理")
@RestController
@RequestMapping(value = "/user", method = {RequestMethod.POST, RequestMethod.GET})
public class UserController {
    @Resource
    private UserRepository userRepository;

    @ApiOperation("用户登录")
    @RequestMapping("/login")
    public ApiResult login(@RequestParam @ApiParam("用户账号") String zh,
                           @RequestParam @ApiParam("用户密码") String mm) {
        if (StringUtils.isEmpty(zh) || StringUtils.isEmpty(mm))
            return ApiResult.FAILURE("用户名或密码不能为空");
        UserEntity userEntity = userRepository.findByZh(zh);
        if (userEntity == null) {
            return ApiResult.FAILURE("账号不存在");
        }
        try {
            if (userEntity.getMm().equals(MD5Util.MD5(mm))) {
                SessionHelper.getSession().setAttribute("user", userEntity);
                return ApiResult.SUCCESS(userEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.FAILURE("密码错误");
    }

    @ApiOperation("添加用户")
    @RequestMapping("/add")
    public ApiResult add(@RequestParam @ApiParam("用户账号") String zh,
                         @RequestParam(defaultValue = "666666") @ApiParam("用户密码") String mm,
                         @RequestParam(required = false) @ApiParam("用户角色") Integer js) {

        if (StringUtils.isNotEmpty(zh)) {
            UserEntity userEntity = userRepository.findByZh(zh);
            if (userEntity != null) {
                return ApiResult.FAILURE("已存在该账号");
            }
            userEntity = new UserEntity();
            userEntity.setZh(zh);
            try {
                userEntity.setMm(MD5Util.MD5(mm));
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResult.FAILURE("密码加密失败");
            }
            userEntity.setJs(js == null ? 0 : js);
            try {
                userRepository.save(userEntity);
                return ApiResult.SUCCESS();
            } catch (Exception e) {
                return ApiResult.FAILURE("添加失败");
            }
        }
        return ApiResult.FAILURE();
    }

    @RequestMapping("/recharge")
    @ApiOperation("充值")
    @MustLogin(rolerequired = {1})
    public ApiResult recharge(@RequestParam @ApiParam("用户id") String id,
                              @RequestParam @ApiParam("充值月数") Integer sl) {
        UserEntity userEntity;
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            return ApiResult.FAILURE("不存在的用户");
        }
        userEntity = optionalUserEntity.get();
        if (userEntity.getJs() == 1)
            return ApiResult.FAILURE("管理员不需设置时限");

        if (sl == 0)
            return ApiResult.SUCCESS();

        userEntity.setDlsx(DateUtils.getNewDate(userEntity.getDlsx(), sl));
        try {
            userRepository.save(userEntity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE("充值失败，请重试");
        }
    }

    @RequestMapping("/findAll")
    @ApiOperation("获取所有用户")
    @MustLogin(rolerequired = {1})
    public ApiResult findAll() {
        return ApiResult.SUCCESS(userRepository.findAll());
    }

    @RequestMapping("/findAllByPage")
    @ApiOperation("分页获取所有用户")
    @MustLogin(rolerequired = {1})
    public ApiResult findAllByPage(@RequestParam @ApiParam("页码") int page, @RequestParam @ApiParam("分页大小") int pagesize) {
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        Page<UserEntity> list = userRepository.findAll(pageable);
        return ApiPageResult.SUCCESS(list.getContent(), page, pagesize, list.getTotalElements(), list.getTotalPages());
    }

    @RequestMapping("/changePassword")
    @ApiOperation("用户修改密码")
    @MustLogin(rolerequired = {0})
    public ApiResult changePassword(@RequestParam() @ApiParam("原密码") String ymm,
                                    @RequestParam() @ApiParam("新密码") String xmm) throws Exception {
        UserEntity userEntity = SessionHelper.getuser();
        Optional<UserEntity> optional = userRepository.findById(userEntity.getId());
        if (!optional.isPresent())
            return ApiResult.FAILURE("用户不存在");
        userEntity = optional.get();
        String oldpass = MD5Util.MD5(ymm);
        if (!userEntity.getMm().equals(oldpass))
            return ApiResult.FAILURE("原始密码输入错误");
        String newpass = MD5Util.MD5(xmm);
        userEntity.setMm(newpass);
        try {
            userRepository.save(userEntity);
            SessionHelper.getSession().removeAttribute("user");
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE("修改密码失败");
        }
    }

    @RequestMapping("/resetPassword")
    @ApiOperation("重置用户密码")
    @MustLogin(rolerequired = {1})
    public ApiResult resetPassword(@RequestParam() @ApiParam("用户id") String id) throws Exception {
        UserEntity userEntity;
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            return ApiResult.FAILURE("不存在的用户");
        }
        userEntity = optionalUserEntity.get();
        userEntity.setMm(MD5Util.MD5("666666"));
        try {
            userRepository.save(userEntity);
            return ApiResult.SUCCESS();
        } catch (Exception e) {
            return ApiResult.FAILURE("重置失败");
        }

    }
}
