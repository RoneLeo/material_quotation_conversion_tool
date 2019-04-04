package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ExcelDataRepository extends CrudRepository<ExcelDataEntity, Integer> {
    List<ExcelDataEntity> findByXmbh(Integer xmbh);

    boolean existsAllByXmbh(Integer xmbh);

    boolean existsByXmbhAndClgg(Integer xmbh, String clgg);

    // 通过id删除
    @Query(value = "delete from excel_data where project_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteByXmbh(Integer xmbh);


    @Query(value = "SELECT a.id,project_id xmbh,goods_name clmc,goods_model clgg,number clsl,goods_unit cldw,CASE WHEN baseprice IS NULL THEN 0 ELSE baseprice END jj,CASE WHEN costprice IS NULL THEN 0 ELSE costprice END cbj FROM (SELECT * from excel_data WHERE project_id = ?1 ) a LEFT JOIN (SELECT project.id, typesize,unit,baseprice,costprice FROM materialdata,project WHERE project.id = ?1 AND  materialdata.uid =project.uid)b ON  goods_model = b.typesize", nativeQuery = true)
    List<Map<String, Object>> findAllByXmbhAndSfid(Integer xmbh);

    @Query(value = "SELECT a.id,project_id xmbh,goods_name clmc,goods_model clgg,number clsl,goods_unit cldw,CASE WHEN baseprice IS NULL THEN 0 ELSE baseprice END jj,CASE WHEN costprice IS NULL THEN 0 ELSE costprice END cbj FROM (SELECT * from excel_data WHERE project_id = ?1 ) a LEFT JOIN (SELECT project.id, typesize,unit,baseprice,costprice FROM materialdata,project WHERE project.id = ?1 AND  materialdata.uid =project.uid)b ON  goods_model = b.typesize", nativeQuery = true,
            countQuery = "SELECT COUNT(*) from excel_data WHERE project_id = ?1 ")
    Page<Map<String, Object>> findAllByXmbhAndSfid(Integer xmbh, Pageable pageable);
}
