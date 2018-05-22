package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.LoginLogMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 15:30:02.
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, Long> implements LoginLogService {

    public LoginLogMapper getActualDao() {
        return (LoginLogMapper)getDao();
    }
}