package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginLogDto;

import java.util.List;

public interface LoginLogMapper extends MyMapper<LoginLog> {

    /**
     * 根据用户输入查询所有登录日志
     * @param dto
     * @return
     */
    List<LoginLog> findByLoginLogDto(LoginLogDto dto);
}