package com.ihrm.system.controller;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.PageUtils;
import com.ihrm.common.utils.ParamCheckUtils;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 20:38
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 添加角色
     *
     * @param role 角色操作参数
     * @return Result
     */
    @PostMapping
    public Result add(@RequestBody Role role) {
        role.setCompanyId(companyId);
        roleService.add(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 更新角色
     *
     * @param id   角色id
     * @param role 角色操作参数
     * @return Result
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role) {
        role.setId(id);
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return Result
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(name = "id") String id) {
        roleService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据ID获取角色信息
     *
     * @param id 角色id
     * @return Result
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable(name = "id") String id) {
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return QueryResultUtils.checkQueryResult(roleResult);
    }

    /**
     * 分页查询角色
     *
     * @param page     当前页码
     * @param pagesize 每页显示的条数
     * @param role     角色操作参数
     * @return Result
     */
    @GetMapping
    public Result findByPage(int page, int pagesize, Role role) {
        QueryPageBean queryPageBean = PageUtils.checkPage(new QueryPageBean(page, pagesize, null));
        Page<Role> rolePage = roleService.findPage(companyId, queryPageBean);
        return QueryResultUtils.checkQueryPageResult(rolePage);
    }

    /**
     * 分配权限
     *
     * @param map 操作参数
     * @return Result
     * @throws CommonException 自定义异常
     */
    @PutMapping(value = "/assignPrem")
    public Result assignPrem(@RequestBody Map<String, Object> map) throws CommonException {
        ParamCheckUtils.checkMapParam(map);
        String roleId = (String) map.get("id");
        List<String> permIds = (List<String>) map.get("permIds");
        roleService.assignPrem(roleId, permIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询所有角色列表
     *
     * @return Result
     */
    @GetMapping(value = "/list")
    public Result findAll() {
        List<Role> roles = roleService.findList(companyId);
        return QueryResultUtils.checkQueryListResult(roles);
    }
}
