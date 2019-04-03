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

    @Query(value = "SELECT excel_data.id,project_id pid,goods_name mc,goods_model gg,number sl,unit dw,baseprice jj,costprice cbj FROM excel_data LEFT JOIN (SELECT id, typesize,unit,baseprice,costprice FROM pro_mate_price,materialdata WHERE id = pro_mate_price.mid AND pid =?2)b ON project_id = ?1 AND goods_model = b.typesize", nativeQuery = true)
    List<Map<String, Object>> findAllByXmbhAndSfid(Integer xmbh, Integer sfid);
}
