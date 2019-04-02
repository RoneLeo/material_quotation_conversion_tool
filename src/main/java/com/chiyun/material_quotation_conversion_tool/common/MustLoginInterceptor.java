package com.chiyun.material_quotation_conversion_tool.common;

import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import com.chiyun.material_quotation_conversion_tool.utils.MessageUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

public class MustLoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod;
        try {
            handlerMethod = (HandlerMethod) handler;
        } catch (Exception e) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        MustLogin annotation = method.getAnnotation(MustLogin.class);
        UserEntity entity = SessionHelper.getuser();
        if (annotation != null) {
            int[] needs = annotation.rolerequired();
            if (entity == null) {
                MessageUtils.resultMsg(response, ApiResult.UNKNOWN());
                return false;
            }
            if (entity.getDlsx() != null && entity.getDlsx().before(new Date())) {
                MessageUtils.resultMsg(response, ApiResult.TimeDown());
                return false;
            }
            for (int need : needs) {
                if (need == 0 || (entity.getJs() == need)) {
                    return true;
                }
            }
            MessageUtils.resultMsg(response, ApiResult.FAILURE("您没有权限进行此操作"));
            return false;
        }
        return true;
    }

}
