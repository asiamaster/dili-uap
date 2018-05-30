package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginLogDto;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-28 11:33:04.
 */
public interface LoginLogService extends BaseService<LoginLog, Long> {
	
	
	EasyuiPageOutput findByLoginLogDto(LoginLogDto loginLog,boolean useProvider) throws Exception;
}