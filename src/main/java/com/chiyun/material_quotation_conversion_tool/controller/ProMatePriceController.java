package com.chiyun.material_quotation_conversion_tool.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wazto on 2019/4/1.
 */
@Api(description = "省份材料管理")
@RestController
@RequestMapping(value = "/promateprice", method = {RequestMethod.POST, RequestMethod.GET})
public class ProMatePriceController {
}
