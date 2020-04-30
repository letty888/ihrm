package com.ihrm.system.service.impl;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 20:39
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

    private final RoleDao roleDao;
    private final IdWorker idWorker;
    private final PermissionDao permissionDao;

    public RoleServiceImpl(RoleDao roleDao, IdWorker idWorker, PermissionDao permissionDao) {
        this.roleDao = roleDao;
        this.idWorker = idWorker;
        this.permissionDao = permissionDao;
    }

    /**
     * 添加角色
     *
     * @param role 角色操作参数
     */
    @Override
    public void add(Role role) {
        String roleId = idWorker.nextId() + "";
        role.setId(roleId);
        roleDao.save(role);
    }

    /**
     * 更新角色
     *
     * @param role 角色操作参数
     */
    @Override
    public void update(Role role) {
        roleDao.save(role);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    @Override
    public void delete(String id) {
        roleDao.deleteById(id);
    }

    /**
     * 根据ID获取角色信息
     *
     * @param id 角色id
     * @return Role
     */
    @Override
    public Role findById(String id) {
        Optional<Role> roleOptional = roleDao.findById(id);
        return roleOptional.orElse(null);
    }

    /**
     * 分页查询角色
     *
     * @param queryPageBean 分页参数
     * @param companyId     企业id
     * @return Page<Role>
     */
    @Override
    public Page<Role> findPage(String companyId, QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        return roleDao.findAll(getSpec(companyId), PageRequest.of(currentPage-1, pageSize));
    }

    /**
     * 分配权限
     *
     * @param roleId  角色id
     * @param permIds 权限ids
     */
    @Override
    public void assignPrem(String roleId, List<String> permIds) throws CommonException {
        Optional<Role> optionalRole = roleDao.findById(roleId);
        if (!optionalRole.isPresent()) {
            throw new CommonException(ResultCode.FAIL);
        }

        Role role = optionalRole.get();
        if (permIds != null && permIds.size() > 0) {
            Set<Permission> permissionSet = new HashSet<>();
            for (String permissionId : permIds) {
                Optional<Permission> optionalPermission = permissionDao.findById(permissionId);
                if (optionalPermission.isPresent()) {
                    Permission permission = optionalPermission.get();
                    permissionSet.add(permission);
                }
            }
            role.setPermissions(permissionSet);
            roleDao.save(role);
        }
    }

    /**
     * 查询所有角色列表
     * @param companyId 公司id
     * @return List<Role>
     */
    @Override
    public List<Role> findList(String companyId) {
        return roleDao.findByCompanyId(companyId);
    }

}
