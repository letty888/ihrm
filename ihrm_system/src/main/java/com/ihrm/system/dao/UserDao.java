package com.ihrm.system.dao;

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {

    /**
     * 根据用户手机号查找对应的用户信息
     * @param mobile 用户手机号
     * @return User
     */
    User findByMobile(String mobile);
}
