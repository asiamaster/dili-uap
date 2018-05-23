package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.domain.Department;
import com.dili.uap.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:10:05.
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

    public DepartmentMapper getActualDao() {
        return (DepartmentMapper)getDao();
    }

    @Override
    @Transactional
    public BaseOutput<Object> insertAfterCheck(Department department) {
        Department record = DTOUtils.newDTO(Department.class);
        record.setName(department.getName());
        record.setFirmCode(department.getFirmCode());
        int count = this.getActualDao().selectCount(record);
        if (count > 0) {
            return BaseOutput.failure("存在相同名称的部门");
        }
        int result = this.getActualDao().insertSelective(department);
        if (result > 0) {
            return BaseOutput.success().setData(department);
        }
        return BaseOutput.failure("插入失败");
    }

    @Override
    @Transactional
    public BaseOutput<Object> updateAfterCheck(Department department) {
        Department record = DTOUtils.newDTO(Department.class);
        record.setName(department.getName());
        record.setFirmCode(department.getFirmCode());
        Department oldDept = this.getActualDao().selectOne(record);
        if (oldDept != null && !oldDept.getId().equals(department.getId())) {
            return BaseOutput.failure("存在相同名称的部门");
        }
        department.setModified(new Date());
        int result = this.getActualDao().updateByPrimaryKey(department);
        if (result > 0) {
            return BaseOutput.success().setData(department);
        }
        return BaseOutput.failure("更新失败");
    }
}