package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExcelDataRepository extends CrudRepository<ExcelDataEntity, String> {
}
