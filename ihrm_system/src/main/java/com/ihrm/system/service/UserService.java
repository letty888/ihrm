package com.ihrm.system.service;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author zhang
 */
public interface UserService {

    /**
     * 添加用户
     *
     * @param user 用户操作参数
     */
    void add(User user);

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    void deleteById(String id);

    /**
     * 更新用户信息
     *
     * @param user 用户操作参数
     */
    void update(User user);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户信息
     * @return User
     */
    User findById(String id);

    /**
     * 条件分页查询
     *
     * @param queryPageBean 分页参数
     * @param map           查询条件
     * @return Page<User>
     */
    Page<User> findPage(QueryPageBean queryPageBean, Map<String, Object> map);

    /**
     * 给用户分配角色
     *
     * @param userId  用户id
     * @param roleIds 角色ids
     */
    void assignRoles(String userId, List<String> roleIds);

    /**
     * 根据用户手机号查找对应的用户信息
     * @param mobile 用户手机号
     * @return User
     */
    User findUserByMobilePhone(String mobile);
}
