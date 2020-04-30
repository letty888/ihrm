package com.ihrm.system.dao;

import com.ihrm.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 20:40
 */
public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    /**
     * 查询所有角色列表
     * @param companyId 公司id
     * @return List<Role>
     */
    List<Role> findByCompanyId(String companyId);
}
