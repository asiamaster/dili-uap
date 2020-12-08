package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.TerminalBinding;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2020-12-08 16:03:34.
 */
public interface TerminalBindingService extends BaseService<TerminalBinding, Long> {

	/**
	 * 根据token绑定终端
	 * 
	 * @param token
	 * @param terminalId TODO
	 * @return
	 */
	BaseOutput<Object> bindByToken(String token, String terminalId);
}