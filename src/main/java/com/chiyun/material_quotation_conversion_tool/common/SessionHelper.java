package com.chiyun.material_quotation_conversion_tool.common;

import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHelper {
    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        return request;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static UserEntity getuser() {
        return (UserEntity) getSession().getAttribute("user");
    }
}
