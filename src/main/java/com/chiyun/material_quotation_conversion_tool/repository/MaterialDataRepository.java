package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.MaterialdataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<MaterialdataEntity> findAllByUid(String uid, Pageable pageable);

    Page<MaterialdataEntity> findAllByUidAndClggLikeAndClmcLike(String uid, String clgg, String clmc, Pageable pageable);

    @Query(value = "SELECT * FROM materialdata WHERE uid = ?2 AND typesize LIKE ?3 AND name LIKE ?4 AND NOT exists(SELECT 1 FROM excel_data WHERE project_id = ?1 AND goods_model = typesize ) ", nativeQuery = true)
    List<MaterialdataEntity> findAllByXmbhAndClggLikeAndClmcLike(Integer xmbh, String uid, String clgg, String clmc);

    @Transactional
    @Modifying
    @Query(value = "DELETE  FROM materialdata WHERE uid = ?1", nativeQuery = true)
    int deleteAllByUid(String uid);

    @Transactional
    @Modifying
    int deleteByUidAndClid(String uid, int clid);

    boolean existsByUidAndClgg(String uid, String clgg);
}
