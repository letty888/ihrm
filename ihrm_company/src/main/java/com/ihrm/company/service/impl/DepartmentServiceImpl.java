package com.ihrm.company.service.impl;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Department;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 10:50
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final IdWorker idWorker;

    public DepartmentServiceImpl(DepartmentDao departmentDao, IdWorker idWorker) {
        this.departmentDao = departmentDao;
        this.idWorker = idWorker;
    }


    /**
     * 添加部门
     *
     * @param department 部门操作参数
     */
    @Override
    public void save(Department department) {
        String departmentId = idWorker.nextId() + "";
        department.setId(departmentId);
        department.setCreateTime(new Date());
        departmentDao.save(department);
    }

    /**
     * 修改部门
     *
     * @param id         部门id
     * @param department 部门操作参数
     */
    @Override
    public void update(String id, Department department) {
        department.setId(id);
        department.setCreateTime(new Date());
        departmentDao.save(department);
    }

    /**
     * 根据部门id删除部门
     *
     * @param id 部门id
     */
    @Override
    public void delete(String id) {
        departmentDao.deleteById(id);
    }

    /**
     * 根据部门id查询对应的部门信息
     *
     * @param id 部门id
     * @return Department
     */
    @Override
    public Department findById(String id) {
        Optional<Department> optionalDepartment = departmentDao.findById(id);
        return optionalDepartment.orElse(null);
    }

    /**
     * 查询所有部门
     *
     * @return List<Department>
     */
    @Override
    public List<Department> findAll() {
        List<Department> departments = departmentDao.findAll();
        return departments;
    }
}
