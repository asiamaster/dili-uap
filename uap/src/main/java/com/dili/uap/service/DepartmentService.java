package com.dili.uap.service;

import java.util.List;
import java.util.Map;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Department;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:10:05.
 */
public interface DepartmentService extends BaseService<Department, Long> {

    /**
     * 新增部门检查
     * @param department
     * @return
     */
    BaseOutput<Department> insertAfterCheck(Department department);


    /**
     * 修改部门检查
     * @param department
     * @return
     */
    BaseOutput<Department> updateAfterCheck(Department department);
    
    
    /**
     * 查询部门列表
     * @param department
     * @return
     */
    List<Map> listDepartments(Department department);
}