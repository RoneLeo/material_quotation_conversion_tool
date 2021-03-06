package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ProjectEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectRepository extends CrudRepository<ProjectEntity, String> {
    // 通过id删除
    @Query(value = "delete from ordertable where id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteById(Integer id);

    // 通过id查询
    ProjectEntity findById(Integer id);

    //查询全部数据
    List<ProjectEntity> findAll();

}
