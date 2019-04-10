package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {
    // 通过id删除
    @Query(value = "delete from project where id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteById(Integer id);

    boolean existsById(Integer xmbh);

    // 通过id查询
    ProjectEntity findById(Integer id);

    //查询全部数据
    @Query(value = "SELECT * FROM project ORDER BY id DESC ", nativeQuery = true)
    List<ProjectEntity> findAllOrderByIdDesc();

    Page<ProjectEntity> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM project WHERE uid = ?1 ORDER BY id DESC ", nativeQuery = true)
    List<ProjectEntity> findAllByUidOrderByIdDesc(String uid);

    Page<ProjectEntity> findAllByUid(String uid, Pageable pageable);

    List<ProjectEntity> findAllByOrderByIdDesc();

}
