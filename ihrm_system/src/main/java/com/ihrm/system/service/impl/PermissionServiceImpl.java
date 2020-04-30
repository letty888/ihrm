package com.ihrm.system.service.impl;

import com.ihrm.common.constants.PermissionConstants;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import com.ihrm.system.service.PermissionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 22:31
 */
@Service
@Transactional(rollbackFor = {CommonException.class, Exception.class})
public class PermissionServiceImpl implements PermissionService {

    private final PermissionDao permissionDao;
    private PermissionApiDao permissionApiDao;
    private PermissionMenuDao permissionMenuDao;
    private PermissionPointDao permissionPointDao;
    private final IdWorker idWorker;

    public PermissionServiceImpl(PermissionDao permissionDao, PermissionMenuDao permissionMenuDao, PermissionPointDao permissionPointDao, PermissionApiDao permissionApiDao, IdWorker idWorker) {
        this.permissionMenuDao = permissionMenuDao;
        this.permissionPointDao = permissionPointDao;
        this.permissionApiDao = permissionApiDao;
        this.permissionDao = permissionDao;
        this.idWorker = idWorker;
    }


    /**
     * 新增权限
     *
     * @param map 权限操作参数
     * @throws Exception 异常
     */
    @Override
    public void save(Map<String, Object> map) throws Exception {
        String permissionId = idWorker.nextId() + "";
        map.put("id", permissionId);
        this.saveOrUpdatePermission(map);
    }

    /**
     * 更新权限
     *
     * @param map 权限操作参数
     * @throws Exception 自定义异常
     */
    @Override
    public void update(Map<String, Object> map) throws Exception {
        this.saveOrUpdatePermission(map);
    }


    /**
     * 查询全部:
     * type      : 查询全部权限列表type：0：菜单 + 按钮（权限点） 1：菜单2：按钮（权限点）3：API接口
     * enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
     * pid ：父id
     *
     * @param map 查询参数
     * @return List<Permission>
     */
    @Override
    public List<Permission> findAll(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Object type =  map.get("type");
            String enVisible = (String)map.get("enVisible");
            String pid = (String) map.get("pid");
            Specification<Permission> specification = (Specification<Permission>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(pid)) {
                    predicateList.add(criteriaBuilder.equal(root.get("pid").as(String.class), pid));
                }
                if (!StringUtils.isEmpty(type)) {
                    Integer typeInteger = Integer.parseInt((String)type);
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if (typeInteger==PermissionConstants.PY_MENU_POINT ) {
                        in.value(PermissionConstants.PY_MENU).value(PermissionConstants.PY_POINT);
                    } else {
                        in.value(typeInteger);
                    }
                    predicateList.add(in);
                }
                if (!StringUtils.isEmpty(enVisible)) {
              //      Integer enVisibleInteger = Integer.parseInt(enVisible);
                    predicateList.add(criteriaBuilder.equal(root.get("enVisible").as(String.class), enVisible));
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            return permissionDao.findAll(specification);
        } else {
            //页面初始化时无条件查询全部权限列表
            return permissionDao.findAll();
        }

    }

    /**
     * 根据id查询对应权限
     *
     * @param id 权限id
     * @return Map<String, Object>
     * @throws CommonException 自定义异常
     */
    @Override
    public Map<String, Object> findById(String id) throws CommonException {
        Optional<Permission> permissionOptional = permissionDao.findById(id);
        if (!permissionOptional.isPresent()) {
            return null;
        }
        Permission permission = permissionOptional.get();
        Integer type = permission.getType();
        //将 permission 转为 map
        Object object;
        if (type == PermissionConstants.PY_MENU) {
            Optional<PermissionMenu> optionalPermissionMenu = permissionMenuDao.findById(id);
            object = optionalPermissionMenu.orElse(null);
        } else if (type == PermissionConstants.PY_POINT) {
            Optional<PermissionPoint> optionalPermissionPoint = permissionPointDao.findById(id);
            object = optionalPermissionPoint.orElse(null);
        } else if (type == PermissionConstants.PY_API) {
            Optional<PermissionApi> optionalPermissionApi = permissionApiDao.findById(id);
            object = optionalPermissionApi.orElse(null);
        } else {
            throw new CommonException(ResultCode.FAIL);
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name", permission.getName());
        map.put("type", permission.getType());
        map.put("code", permission.getCode());
        map.put("description", permission.getDescription());
        map.put("pid", permission.getPid());
        map.put("enVisible", permission.getEnVisible());
        return map;
    }

    /**
     * 根据id删除对应权限
     *
     * @param id 权限id
     */
    @Override
    public void delete(String id) throws CommonException {
        //首先根据id查询出对应的权限
        Optional<Permission> optionalPermission = permissionDao.findById(id);
        if (!optionalPermission.isPresent()) {
            throw new CommonException(ResultCode.NO_DATA);
        }
        Permission permission = optionalPermission.get();
        Integer type = permission.getType();
        if (type == PermissionConstants.PY_MENU) {
            permissionMenuDao.deleteById(id);
        } else if (type == PermissionConstants.PY_API) {
            permissionApiDao.deleteById(id);
        } else if (type == PermissionConstants.PY_POINT) {
            permissionPointDao.deleteById(id);
        } else {
            throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.deleteById(id);
    }


    /**
     * 保存或更新权限的私有方法
     *
     * @param map 权限的操作参数
     * @throws Exception 异常
     */
    private void saveOrUpdatePermission(Map<String, Object> map) throws Exception {
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        Integer permissionType = permission.getType();
        switch (permissionType) {
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw new CommonException(ResultCode.PARAMETER_ERROR);
        }
        permissionDao.save(permission);
    }
}
