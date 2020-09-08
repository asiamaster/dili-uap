package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.EditFirmAdminUserDto;
import com.dili.uap.domain.dto.FirmAddDto;
import com.dili.uap.domain.dto.FirmUpdateDto;
import com.dili.uap.sdk.domain.Firm;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 14:31:07.
 */
public interface FirmService extends BaseService<Firm, Long> {

	/**
	 * 新增市场并绑定用户数据权限
	 * 
	 * @param firm
	 * @return
	 */
	BaseOutput<Object> insertAndBindUserDataAuth(FirmAddDto firm);

	/**
	 * 修改市场验证编码是否重复
	 * 
	 * @param dto
	 * @return
	 */
	BaseOutput<Object> updateSelectiveAfterCheck(FirmUpdateDto dto);

	/**
	 * 根据编码查询id
	 * 
	 * @param firmCode
	 * @return
	 */
	Firm getIdByCode(String firmCode);

	/**
	 * 设置超级管理员用户
	 * 
	 * @param dto
	 * @return
	 */
	BaseOutput<Object> updateAdminUser(EditFirmAdminUserDto dto);

	/**
	 * 启用
	 * 
	 * @param id
	 * @return
	 */
	BaseOutput<Object> enable(Long id);

	/**
	 * 禁用
	 * 
	 * @param id
	 * @return
	 */
	BaseOutput<Object> disable(Long id);
}