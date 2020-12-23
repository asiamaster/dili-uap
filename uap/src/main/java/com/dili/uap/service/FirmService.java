package com.dili.uap.service;

import java.util.List;

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
	BaseOutput<Object> addFirm(FirmAddDto firm);

	/**
	 * 修改市场验证编码是否重复
	 * 
	 * @param dto
	 * @return
	 */
	BaseOutput<Object> updateSelectiveAfterCheck(FirmUpdateDto dto);

	/**
	 * 根据编码查询
	 * 
	 * @param firmCode
	 * @return
	 */
	Firm getByCode(String firmCode);

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

	/**
	 * 根据父级商户id获取 所有子商户（级联）
	 * 
	 * @param parentId
	 * @return
	 */
	List<Firm> getAllChildrenByParentId(Long parentId);

	/**
	 * 提交审批
	 * 
	 * @param id     商户id
	 * @param taskId TODO
	 * @return
	 */
	BaseOutput<Object> submit(Long id, String taskId);

	/**
	 * 审批拒绝
	 * 
	 * @param id         商户id
	 * @param taskId     流程任务id
	 * @param approverId TODO
	 * @param notes      TODO
	 * @return
	 */
	BaseOutput<Object> reject(Long id, String taskId, Long approverId, String notes);

	/**
	 * 审批通过
	 * 
	 * @param id         商户id
	 * @param taskId     流程任务id
	 * @param approverId TODO
	 * @param notes      TODO
	 * @return
	 */
	BaseOutput<Object> accept(Long id, String taskId, Long approverId, String notes);

	/**
	 * 删除市场并终止流程
	 * 
	 * @param id     市场id
	 * @param taskId 流程任务id
	 * @return
	 */
	BaseOutput<Object> deleteAndStopProcess(Long id, String taskId);

	/**
	 * 保存并提交
	 * @param dto TODO
	 * @return
	 */
	BaseOutput<Object> saveAndSubmit(FirmUpdateDto dto);
}