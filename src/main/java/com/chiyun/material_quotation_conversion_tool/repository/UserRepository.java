package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wazto on 2019/4/2.
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByZh(String zh);
}
