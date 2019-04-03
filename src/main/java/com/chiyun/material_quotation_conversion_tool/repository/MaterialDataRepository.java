package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wazto on 2019/4/1.
 */
public interface MaterialDataRepository extends JpaRepository<MaterialdataEntity, Integer> {
    List<MaterialdataEntity> findAllByUid(String uid);

    @Transactional
    @Modifying
    @Query(value = "DELETE  FROM materialdata WHERE uid = ?1", nativeQuery = true)
    int deleteAllByUid(String uid);

    @Transactional
    @Modifying
    int deleteByUidAndClid(String uid, int clid);
}
