package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.Department;
import com.dili.uap.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-23 13:54:13.
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

    public DepartmentMapper getActualDao() {
        return (DepartmentMapper)getDao();
    }
}