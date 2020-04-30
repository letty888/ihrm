package com.ihrm.system.service;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.Role;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 20:39
 */
public interface RoleService {

    /**
     * 添加角色
     *
     * @param role 角色操作参数
     */
    void add(Role role);

    /**
     * 更新角色
     *
     * @param role 角色操作参数
     */
    void update(Role role);

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void delete(String id);

    /**
     * 根据ID获取角色信息
     *
     * @param id 角色id
     * @return Role
     */
    Role findById(String id);

    /**
     * 分页查询角色
     *
     * @param queryPageBean 分页参数
     * @param companyId 企业id
     * @return Page<Role>
     */
    Page<Role> findPage(String companyId,QueryPageBean queryPageBean);

    /**
     * 分配权限
     * @param roleId 角色id
     * @param permIds 权限ids
     */
    void assignPrem(String roleId, List<String> permIds) throws CommonException;

    /**
     * 查询所有角色列表
     * @param companyId 公司id
     * @return List<Role>
     */
    List<Role> findList(String companyId);
}
