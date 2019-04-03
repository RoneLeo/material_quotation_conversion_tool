package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by wazto on 2019/4/1.
 */
public interface ProMatePriceRepository extends JpaRepository<ProMatePriceEntity, ProMatePriceEntityPK> {
    List<ProMatePriceEntity> findAllBySfid(Integer sfid);

    @Query(value = "SELECT pid sfid,mid clid ,name mc,typesize gg,unit dw,baseprice jj,costprice cbj FROM materialdata,pro_mate_price WHERE pid = ?1 AND mid = materialdata.id", nativeQuery = true)
    List<Map<String, Object>> findAllInfoBySfid(Integer sfid);

    boolean existsBySfid(Integer sfid);

    @Transactional
    int deleteAllBySfid(Integer sfid);
}
