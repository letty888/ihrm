package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhang
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {

    private final DepartmentService departmentService;
    private final CompanyService companyService;

    public DepartmentController(DepartmentService departmentService, CompanyService companyService) {
        this.departmentService = departmentService;
        this.companyService = companyService;
    }

    /**
     * 添加部门
     *
     * @param department 部门操作参数
     * @return Result
     */
    @PostMapping(value = "/department")
    public Result add(@RequestBody Department department) {
        department.setCompanyId(companyId);
        departmentService.save(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 修改部门
     *
     * @param id         部门id
     * @param department 部门操作参数
     * @return Result
     */
    @PutMapping(value = "/department/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Department department) {
        department.setCompanyId(companyId);
        departmentService.update(id, department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id删除对应部门
     *
     * @param id 部门id
     * @return Result
     */
    @DeleteMapping("/department/{id}")
    public Result delete(@PathVariable("id") String id) {
        departmentService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id查询对应的部门信息
     *
     * @param id 部门id
     * @return Result
     */
    @GetMapping("/department/{id}")
    public Result findById(@PathVariable("id") String id) {
        Department department = departmentService.findById(id);
        return QueryResultUtils.checkQueryResult(department);
    }

    /**
     * 查询所有部门
     *
     * @return Result
     */
    @GetMapping("/department")
    public Result findAll() {
        Company company = companyService.findById(companyId);
        List<Department> departments = departmentService.findAll();
        return new Result(ResultCode.SUCCESS, new DeptListResult(company, departments));
    }
}
