package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import io.swagger.models.auth.In;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ExcelDataRepository extends CrudRepository<ExcelDataEntity, String> {
    List<ExcelDataEntity> findByXmbh(Integer xmbh);
}
