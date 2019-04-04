//package com.chiyun.material_quotation_conversion_tool.controller;
//
//import com.chiyun.material_quotation_conversion_tool.common.ApiResult;
//import com.chiyun.material_quotation_conversion_tool.entity.ProvinceEntity;
//import com.chiyun.material_quotation_conversion_tool.repository.ProMatePriceRepository;
//import com.chiyun.material_quotation_conversion_tool.repository.ProvinceRepository;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * Created by wazto on 2019/4/1.
// */
//@Api(description = "省份管理")
//@RestController
//@RequestMapping(value = "/province", method = {RequestMethod.POST, RequestMethod.GET})
//public class ProvinceController {
//    @Resource
//    private ProvinceRepository provinceRepository;
//    @Resource
//    private ProMatePriceRepository proMatePriceRepository;
//
//    @RequestMapping("/findAll")
//    @ApiOperation("获取全部省份信息")
//    public ApiResult findAll() {
//        return ApiResult.SUCCESS(provinceRepository.findAll());
//    }
//
//    @RequestMapping("/add")
//    @ApiOperation("添加省份信息")
//    public ApiResult add(@RequestParam @ApiParam("省份名称") String sf) {
//
//        ProvinceEntity entity = new ProvinceEntity();
//        entity.setMc(sf);
//        try {
//            provinceRepository.save(entity);
//            return ApiResult.SUCCESS(entity);
//        } catch (Exception e) {
//            return ApiResult.FAILURE("已存在该省份");
//        }
//    }
//
//    @RequestMapping("/delete")
//    @ApiOperation("删除省份信息")
//    public ApiResult delete(@RequestParam @ApiParam("省份id") Integer id) {
//        if (proMatePriceRepository.existsBySfid(id))
//            return ApiResult.FAILURE("该省份存在价目数据，不能删除");
//        if (provinceRepository.existsById(id))
//            try {
//                provinceRepository.deleteById(id);
//                return ApiResult.SUCCESS();
//            } catch (Exception e) {
//                return ApiResult.FAILURE("删除失败");
//            }
//        return ApiResult.FAILURE("该省份已删除");
//    }
//}
