package com.dili.uap.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.LoginLogDto;

public interface LoginLogMapper extends MyMapper<LoginLog> {

    /**
     * 根据用户输入查询所有登录日志
     * @param dto
     * @return
     */
	public List<LoginLog>findByLoginLogDto(LoginLogDto dto);
}