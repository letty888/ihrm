package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.ParamCheckUtils;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author zhang
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/sys/permission")
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    /**
     * 新增权限
     *
     * @param map 嫌憎权限的参数
     * @return Result
     */
    @PostMapping
    public Result save(@RequestBody Map<String, Object> map) throws Exception {
        ParamCheckUtils.checkMapParam(map);
        permissionService.save(map);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 修改权限
     *
     * @param id  权限id
     * @param map 权限操作参数
     * @return Result
     * @throws Exception 自定义异常
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Map<String, Object> map) throws Exception {
        ParamCheckUtils.checkMapParam(map);
        map.put("id", id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 查询权限列表
     *
     * @param map 查询参数
     * @return Result
     * @throws CommonException 自定义异常
     */
    @GetMapping
    public Result findAll(@RequestParam Map<String, Object> map) throws CommonException {
        //页面初始化时不会携带参数查询,而是查询全部,所以这里不能对map参数进行为空校验
        List<Permission> permissionList = permissionService.findAll(map);
        return QueryResultUtils.checkQueryListResult(permissionList);
    }


    /**
     * 根据ID查询
     *
     * @param id 权限id
     * @return Result
     * @throws CommonException 自定义异常
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id") String id) throws CommonException {
        Map<String, Object> map = permissionService.findById(id);
        return QueryResultUtils.checkQueryMapResult(map);
    }

    /**
     * 根据id删除对应权限
     *
     * @param id 权限id
     * @return Result
     * @throws CommonException 自定义异常
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(value = "id") String id) throws CommonException {
        permissionService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }


}
