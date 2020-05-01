package com.ihrm.company.service;

import com.ihrm.domain.company.Company;

import java.util.List;

/**
 * @author zhang
 */
public interface CompanyService {

    /**
     * 添加企业
     *
     * @param company 企业操作参数
     */
    void add(Company company);

    /**
     * 更新企业数据
     *
     * @param id      企业id
     * @param company 企业操作参数
     */
    void update(String id, Company company);

    /**
     * 根据企业id删除企业
     *
     * @param id 企业id
     */
    void delete(String id);

    /**
     * 根据企业id查询企业
     *
     * @param id 企业id
     * @return Company
     */
    Company findById(String id);

    /**
     * 获取所有企业
     *
     * @return List<Company>
     */
    List<Company> findAll();
}
