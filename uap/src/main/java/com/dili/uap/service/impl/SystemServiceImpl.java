package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.SystemMapper;
import com.dili.uap.domain.System;
import com.dili.uap.service.SystemService;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:24:56.
 */
@Service
public class SystemServiceImpl extends BaseServiceImpl<System, Long> implements SystemService {

    public SystemMapper getActualDao() {
        return (SystemMapper)getDao();
    }

    @Override
    @Transactional
	public BaseOutput<Object> insertAfterCheck(System system) {
		System systemNameCondition = DTOUtils.newDTO(System.class);
		systemNameCondition.setName(system.getName());
		systemNameCondition.setFirmCode(system.getFirmCode());
		
		
        int systemNameCount = this.getActualDao().selectCount(systemNameCondition);
        if (systemNameCount > 0) {
            return BaseOutput.failure("存在相同名称的系统");
        }
        
		System systemCodeCondition = DTOUtils.newDTO(System.class);
		systemCodeCondition.setCode(system.getCode());
		systemCodeCondition.setFirmCode(system.getFirmCode());
        int systemCodeCount = this.getActualDao().selectCount(systemNameCondition);
        if (systemCodeCount > 0) {
            return BaseOutput.failure("存在相同编码的系统");
        }
        
		
        int result = this.getActualDao().insertSelective(system);
        if (result > 0) {
            return BaseOutput.success().setData(system);
        }
        return BaseOutput.failure("插入失败");
	}

    @Override
    @Transactional
	public BaseOutput<Object> updateAfterCheck(System system) {
		System systemNameCondition = DTOUtils.newDTO(System.class);
		systemNameCondition.setName(system.getName());
		systemNameCondition.setFirmCode(system.getFirmCode());
		
		
		System systemWithSameName = this.getActualDao().selectOne(systemNameCondition);
        if (systemWithSameName!=null&&!systemWithSameName.getId().equals(system.getId())) {
            return BaseOutput.failure("存在相同名称的系统");
        }
        
		System systemCodeCondition = DTOUtils.newDTO(System.class);
		systemCodeCondition.setCode(system.getCode());
		systemCodeCondition.setFirmCode(system.getFirmCode());
		System systemWithSameCode = this.getActualDao().selectOne(systemNameCondition);
		if (systemWithSameCode!=null&&!systemWithSameCode.getId().equals(system.getId())) {
            return BaseOutput.failure("存在相同编码的系统");
        }
        
        system.setModified(new Date());
        int result = this.updateExactSimple(system);
        if (result > 0) {
            return BaseOutput.success().setData(system);
        }
        return BaseOutput.failure("更新失败");
	}
}