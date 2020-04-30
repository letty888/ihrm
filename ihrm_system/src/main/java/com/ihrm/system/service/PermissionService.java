package com.ihrm.system.service;

import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.Permission;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 22:30
 */
public interface PermissionService {

    /**
     * 新增权限
     *
     * @param map 权限操作参数
     * @throws Exception 异常
     */
    void save(Map<String, Object> map) throws Exception;

    /**
     * 更新权限
     *
     * @param map 权限操作参数
     * @throws Exception 异常
     */
    void update(Map<String, Object> map) throws Exception;

    /**
     * 查询权限列表
     *
     * @param map 查询参数
     * @return List<Permission>
     */
    List<Permission> findAll(Map<String, Object> map);

    /**
     * 根据id查询对应权限
     *
     * @param id 权限id
     * @return Map<String, Object>
     * @throws CommonException 自定义异常
     */
    Map<String, Object> findById(String id) throws CommonException;

    /**
     * 根据id删除对应权限
     *
     * @param id 权限id
     * @throws CommonException 自定义异常
     */
    void delete(String id) throws CommonException;

}
