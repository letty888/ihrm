package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author zhang
 */
public interface DepartmentService {

    /**
     * 添加部门
     *
     * @param department 部门操作参数
     */
    void save(Department department);

    /**
     * 修改部门
     *
     * @param id         部门id
     * @param department 部门操作参数
     */
    void update(String id, Department department);

    /**
     * 根据部门id删除部门
     *
     * @param id 部门id
     */
    void delete(String id);


    /**
     * 根据部门id查询对应的部门信息
     * @param id 部门id
     * @return Department
     */
    Department findById(String id);

    /**
     * 查询所有部门
     * @return List<Department>
     */
    List<Department> findAll();
}
