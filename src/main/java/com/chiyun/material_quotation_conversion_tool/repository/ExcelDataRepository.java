package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ExcelDataRepository extends CrudRepository<ExcelDataEntity, String> {
    List<ExcelDataEntity> findByXmbh(Integer xmbh);

    // 通过id删除
    @Query(value = "delete from excel_data where project_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteByXmbh(Integer xmbh);
}
