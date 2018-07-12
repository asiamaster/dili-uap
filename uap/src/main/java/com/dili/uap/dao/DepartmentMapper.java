package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper extends MyMapper<Department> {
    
    /**
     * 查询部门列表(id以及parent带有前辍)
     * @param department
     * @return
     */
    List<Map> listDepartments(Department department);
}