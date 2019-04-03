package com.chiyun.material_quotation_conversion_tool.controller;

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
                         @RequestParam @ApiParam("用户所属省份id") Integer sfid,
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
            userEntity.setSfid(sfid == null ? 0 : sfid);
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
}
