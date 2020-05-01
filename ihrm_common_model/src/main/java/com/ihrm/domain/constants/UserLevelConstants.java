package com.ihrm.domain.constants;

import java.io.Serializable;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/30 11:51
 * 用户级别：saasAdmin，coAdmin，user
 */
public class UserLevelConstants implements Serializable {

    private static final long serialVersionUID = 435394557905490433L;

    /**
     * saas平台管理员:拥有所有权限
     */
    public final static String SAASADMIN = "saasAdmin";

    /**
     * 企业管理员:拥有企业相关的所有权限
     */
    public final static String COADIM = "coAdmin";

    /**
     * 企业普通用户:用户已经分配角色的权限
     */
    public final static String USER = "user";
}
