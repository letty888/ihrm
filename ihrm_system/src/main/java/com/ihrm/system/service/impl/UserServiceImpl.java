package com.ihrm.system.service.impl;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 15:42
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final IdWorker idWorker;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, IdWorker idWorker, RoleDao roleDao) {
        this.userDao = userDao;
        this.idWorker = idWorker;
        this.roleDao = roleDao;
    }

    /**
     * 添加用户
     *
     * @param user 用户操作参数
     */
    @Override
    public void add(User user) {
        String userId = idWorker.nextId() + "";
        user.setId(userId);
        //设置默认登录密码
        user.setPassword("123456");
        user.setCreateTime(new Date());
        user.setEnableState(1);
        userDao.save(user);
    }

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户操作参数
     */
    @Override
    public void update(User user) {
        user.setCreateTime(new Date());
        userDao.save(user);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户信息
     * @return User
     */
    @Override
    public User findById(String id) {
        Optional<User> optionalUser = userDao.findById(id);
        return optionalUser.orElse(null);
    }

    /**
     * 条件分页查询
     *
     * @param queryPageBean 分页参数
     * @param map           查询条件
     * @return Page<User>
     */
    @Override
    public Page<User> findPage(QueryPageBean queryPageBean, Map<String, Object> map) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String companyId = null;
        String hasDept = null;
        String departmentId = null;
        if (map != null && map.size() > 0) {
            companyId = (String) map.get("companyId");
            hasDept = (String) map.get("hasDept");
            departmentId = (String) map.get("departmentId");
        }

        String finalCompanyId = companyId;
        String finalDepartmentId = departmentId;
        String finalHasDept = hasDept;
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(finalCompanyId)) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), finalCompanyId));
                }
                if (!StringUtils.isEmpty(finalDepartmentId)) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), finalDepartmentId));
                }
                if (!StringUtils.isEmpty(finalHasDept)) {
                    //0 未分配(departmentId = null); 1 已分配
                    if ("0".equals(finalHasDept)) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };

        return userDao.findAll(specification, PageRequest.of(currentPage - 1, pageSize));
    }

    @Override
    public void assignRoles(String userId, List<String> roleIds) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (roleIds != null && roleIds.size() > 0) {
                Set<Role> roleSet = new HashSet<>(0);
                for (String roleId : roleIds) {
                    Optional<Role> optionalRole = roleDao.findById(roleId);
                    if (optionalRole.isPresent()) {
                        Role role = optionalRole.get();
                        roleSet.add(role);
                    }
                }
                user.setRoles(roleSet);
                userDao.save(user);
            }
        }
    }

    /**
     * 根据用户手机号查找对应的用户信息
     * @param mobile 用户手机号
     * @return User
     */
    @Override
    public User findUserByMobilePhone(String mobile) {
        return userDao.findByMobile(mobile);
    }


}
