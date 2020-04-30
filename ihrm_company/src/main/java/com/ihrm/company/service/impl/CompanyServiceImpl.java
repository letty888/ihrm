package com.ihrm.company.service.impl;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 10:51
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;
    private final IdWorker idWorker;

    public CompanyServiceImpl(CompanyDao companyDao, IdWorker idWorker) {
        this.companyDao = companyDao;
        this.idWorker = idWorker;
    }

    /**
     * 添加企业
     *
     * @param company 企业操作参数
     */
    @Override
    public void add(Company company) {
        String companyId = idWorker.nextId() + "";
        company.setId(companyId);
        company.setCreateTime(new Date());
        //启用
        company.setState(1);
        company.setBalance(0d);
        //未审核
        company.setAuditState("0");
        companyDao.save(company);
    }


    /**
     * 更新企业数据
     *
     * @param id      企业id
     * @param company 企业操作参数
     */
    @Override
    public void update(String id, Company company) {
        company.setId(id);
        company.setCreateTime(new Date());
        companyDao.save(company);
    }

    /**
     * 根据企业id删除企业
     *
     * @param id 企业id
     */
    @Override
    public void delete(String id) {
        companyDao.deleteById(id);
    }

    /**
     * 根据企业id查询企业
     *
     * @param id 企业id
     * @return Company
     */
    @Override
    public Company findById(String id) {
        Optional<Company> optional = companyDao.findById(id);
        return optional.orElse(null);
    }


    /**
     * 获取所有企业
     *
     * @return List<Company>
     */
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }
}
