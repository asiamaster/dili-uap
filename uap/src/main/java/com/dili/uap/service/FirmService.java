package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
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
	BaseOutput<Object> insertAndBindUserDataAuth(Firm firm);

	/**
	 * 修改市场验证编码是否重复
	 * 
	 * @param firm
	 * @return
	 */
	BaseOutput<Object> updateSelectiveAfterCheck(Firm firm);
}