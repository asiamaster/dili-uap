package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.domain.Department;
import com.dili.uap.domain.Firm;
import com.dili.uap.service.DepartmentService;
import com.dili.uap.service.FirmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:10:05.
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

	 @Autowired FirmMapper firmMapper;
    public DepartmentMapper getActualDao() {
        return (DepartmentMapper)getDao();
    }
    
    /**
     * 部门相同时，返回一个错误信息字符串
     * @param department
     * @return 相同市场存在相同部门的错误信息
     */
    private String buildErrorMessage(Department department) {
		String firmName = "";
		Firm condition = DTOUtils.newDTO(Firm.class);
		condition.setCode(department.getFirmCode());
		
		Firm firm = firmMapper.selectOne(condition);
		if (firm != null) {
			firmName = firm.getName();
		}

		StringBuilder sb = new StringBuilder()
				.append("[").append(firmName).append("]")
				.append("市场下存在相同的部门")
				.append("[").append(department.getName()).append("]");
		return sb.toString();
    	
    }
    @Override
    @Transactional
    public BaseOutput<Department> insertAfterCheck(Department department) {
        Department record = DTOUtils.newDTO(Department.class);
        record.setName(department.getName());
        record.setFirmCode(department.getFirmCode());
        int count = this.getActualDao().selectCount(record);
        if (count > 0) {
            return BaseOutput.failure(this.buildErrorMessage(department));
        }
        Date now=new Date();
        department.setCreated(now);
        department.setModified(now);
        
        int result = this.getActualDao().insertSelective(department);
        department.setCode(department.getFirmCode() + "-" + department.getId());
        this.getActualDao().updateByPrimaryKey(department);
        if (result > 0) {
            return BaseOutput.success().setData(department);
        }
        return BaseOutput.failure("插入失败");
    }

    @Override
    @Transactional
    public BaseOutput<Department> updateAfterCheck(Department department) {
        Department record = DTOUtils.newDTO(Department.class);
        record.setName(department.getName());
        record.setFirmCode(department.getFirmCode());
        Department oldDept = this.getActualDao().selectOne(record);
        if (oldDept != null && !oldDept.getId().equals(department.getId())) {
        	 return BaseOutput.failure(this.buildErrorMessage(department));
        }
        department.setModified(new Date());
        int result = this.getActualDao().updateByPrimaryKey(department);
        if (result > 0) {
            return BaseOutput.success().setData(department);
        }
        return BaseOutput.failure("更新失败");
    }
   
    
	@Override
	public List<Map> listDepartments(Department department) {
		return this.getActualDao().listDepartments(department);
	}
}