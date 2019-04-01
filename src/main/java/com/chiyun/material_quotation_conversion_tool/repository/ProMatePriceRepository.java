package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntity;
import com.chiyun.material_quotation_conversion_tool.entity.ProMatePriceEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wazto on 2019/4/1.
 */
public interface ProMatePriceRepository extends JpaRepository<ProMatePriceEntity, ProMatePriceEntityPK> {
//    List<ProMatePriceEntity> findAllBySfid();
}
