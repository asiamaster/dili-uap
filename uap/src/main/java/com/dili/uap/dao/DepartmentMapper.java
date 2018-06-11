package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.domain.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper extends MyMapper<Department> {

    /**
     * 根据用户id查询所有部门
     * @param userId
     * @return
     */
    List<Department> listByUserId(Long userId);

    /**
     * 根据用户id和市场编码(firm_code，可选)查询所有部门数据权限
     * @param param key:userId(必填)和firmCode(选填)
     * @return
     */
    List<DataAuth> listDataAuthes(Map<String, Object> param);
    
    /**
     * 查询部门列表(id以及parent带有前辍)
     * @param department
     * @return
     */
    List<Map> listDepartments(Department department);
}