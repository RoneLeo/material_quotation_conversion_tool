package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wazto on 2019/4/1.
 */
public interface MaterialDataRepository extends JpaRepository<MaterialdataEntity, Integer> {
}
