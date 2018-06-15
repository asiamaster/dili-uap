package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.dao.SystemMapper;
import com.dili.uap.domain.DataDictionary;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.System;
import com.dili.uap.domain.SystemConfig;
import com.dili.uap.service.SystemService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:24:56.
 */
@Service
public class SystemServiceImpl extends BaseServiceImpl<System, Long> implements SystemService {

	@Autowired DataDictionaryMapper dataDictionaryMapper;
	@Autowired SystemConfigMapper systemConfigMapper;
	@Autowired MenuMapper menuMapper;
	
	public SystemMapper getActualDao() {
		return (SystemMapper) getDao();
	}

	@Override
	@Transactional
	public BaseOutput<Object> insertAfterCheck(System system) {
		
		//根据名称和系统查询
		System systemNameCondition = DTOUtils.newDTO(System.class);
		systemNameCondition.setName(system.getName());
//		systemNameCondition.setFirmCode(system.getFirmCode());

		int systemNameCount = this.getActualDao().selectCount(systemNameCondition);
		if (systemNameCount > 0) {
			return BaseOutput.failure("存在相同名称的系统");
		}
		//根据编码和系统查询
		System systemCodeCondition = DTOUtils.newDTO(System.class);
		systemCodeCondition.setCode(system.getCode());
//		systemCodeCondition.setFirmCode(system.getFirmCode());
		
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
		
		//根据名称和系统查询
		System systemNameCondition = DTOUtils.newDTO(System.class);
		systemNameCondition.setName(system.getName());
//		systemNameCondition.setFirmCode(system.getFirmCode());

		System systemWithSameName = this.getActualDao().selectOne(systemNameCondition);
		if (systemWithSameName != null && !systemWithSameName.getId().equals(system.getId())) {
			return BaseOutput.failure("存在相同名称的系统");
		}
		//根据编码和系统查询
		System systemCodeCondition = DTOUtils.newDTO(System.class);
		systemCodeCondition.setCode(system.getCode());
//		systemCodeCondition.setFirmCode(system.getFirmCode());
		
		System systemWithSameCode = this.getActualDao().selectOne(systemNameCondition);
		if (systemWithSameCode != null && !systemWithSameCode.getId().equals(system.getId())) {
			return BaseOutput.failure("存在相同编码的系统");
		}

		
		//修改了code的时候要对关联的其他表(基于code关联)数据进行判断
		System systemInDB = this.get(system.getId());
		if (systemInDB != null && !system.getCode().equals(system.getCode())) {

			if (this.hasDataDictionary(system)) {
				return BaseOutput.failure("系统关联了[数据字典]，不能修改编码");
			}

			if (this.hasSystemConfig(system)) {
				return BaseOutput.failure("系统关联了[系统配置]，不能修改编码");
			}
		}
		
		
		system.setModified(new Date());
		int result = this.updateExactSimple(system);
		if (result > 0) {
			return BaseOutput.success().setData(system);
		}
		return BaseOutput.failure("更新失败");
	}
	@Override
	@Transactional
	public BaseOutput<Object> deleteAfterCheck(Long id) {
		
		
		System system=this.get(id);
		if (system != null) {
			if (this.hasDataDictionary(system)) {
				return BaseOutput.failure("系统关联了[数据字典]，不能删除");
			}

			if (this.hasSystemConfig(system)) {
				return BaseOutput.failure("系统关联了[系统配置]，不能删除");
			}

			if (this.hasMenu(system)) {
				return BaseOutput.failure("系统关联了[菜单]，不能删除");
			}
		}
		super.delete(id);
		return BaseOutput.success("删除成功");
	}

	@Override
	public List<System> listByUserId(Long userId) {
		return getActualDao().listByUserId(userId);
	}

	/**
	 * 是否通过code关系了字典数据(DataDictionary)
	 * @param system
	 * @return
	 */
	private boolean hasDataDictionary(System system) {
		DataDictionary dataDictionaryCondition = DTOUtils.newDTO(DataDictionary.class);
		dataDictionaryCondition.setSystemCode(system.getCode());
		return this.dataDictionaryMapper.selectCount(dataDictionaryCondition) > 0;
	}
	/**
	 * 是否通过code关系了系统配置(SystemConfig)
	 * @param system
	 * @return
	 */
	private boolean hasSystemConfig(System system) {
		SystemConfig systemConfigCondition = DTOUtils.newDTO(SystemConfig.class);
		systemConfigCondition.setSystemCode(system.getCode());
		return this.systemConfigMapper.selectCount(systemConfigCondition) > 0;
	}
	/**
	 * 是否通过id关系了菜单(Menu)
	 * @param system
	 * @return
	 */
	private boolean hasMenu(System system) {
		Menu menuCondition = DTOUtils.newDTO(Menu.class);
		menuCondition.setSystemId(system.getId());
		return this.menuMapper.selectCount(menuCondition) > 0;
	}
}