package com.chiyun.material_quotation_conversion_tool.controller;

import com.chiyun.material_quotation_conversion_tool.common.MustLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by wazto on 2019/4/10.
 */
@RequestMapping(value = "/file", method = {RequestMethod.POST, RequestMethod.GET})
@Api("文件模版")
@RestController
public class FileController {
    @RequestMapping("/getModelFile")
    @MustLogin(rolerequired = {0})
    public void getModelFile(@RequestParam @ApiParam("类型：1-项目材料，2-材料价目") int lx, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        String filename = lx == 1 ? "项目材料导入模板.xls" : "材料指导价模板.xls";
        String userAgent = request.getHeader("USER-AGENT");
        String newFilename = URLEncoder.encode(filename, "UTF-8").replace("+", " ");
        if (userAgent != null) {
            if (userAgent.contains("edge") || userAgent.contains("Edge") || userAgent.contains("Trident") || userAgent.contains("trident")) {
                newFilename = URLEncoder.encode(filename, "UTF-8").replace("+", " ");
            } else {
                newFilename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
        }
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + newFilename);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
