package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author zhang
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * 添加企业
     *
     * @param company 企业操作参数
     * @return Result
     */
    @PostMapping
    public Result add(@RequestBody Company company) {
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id更新企业信息
     *
     * @param id      企业id
     * @param company 企业操作参数
     * @return Result
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Company company) {
        companyService.update(id, company);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据企业id删除企业
     *
     * @param id 企业id
     * @return Result
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(name = "id") String id) {
        companyService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据企业id查询对应信息
     *
     * @param id 企业id
     * @return Result
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") String id) {
        Company company = companyService.findById(id);
        return QueryResultUtils.checkQueryResult(company);
    }

    /**
     * 获取所有企业
     *
     * @return Result
     */
    @GetMapping
    public Result findList() {
        List<Company> companyList = companyService.findAll();
        return QueryResultUtils.checkQueryListResult(companyList);
    }
}
