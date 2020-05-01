package com.ihrm.system.service;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 根据用户手机号查询用户信息
     * @param mobile 用户手机号
     * @return User
     */
    User findByMobile(String mobile);

    /**
     * 用户头像上传
     * @param id 用户id
     * @param file 用户头像文件
     * @return imageUrl
     */
    String upload(String id, MultipartFile file) throws Exception;
}
