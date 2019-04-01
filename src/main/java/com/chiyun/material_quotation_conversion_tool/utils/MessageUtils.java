package com.chiyun.material_quotation_conversion_tool.utils;

import com.alibaba.fastjson.JSONObject;
import com.chiyun.material_quotation_conversion_tool.common.ApiResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageUtils {
    public static void resultMsg(HttpServletResponse response, ApiResult<Object> result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw = response.getWriter();
        String resultjson = JSONObject.toJSONString(result);
        pw.write(resultjson);
    }
}
