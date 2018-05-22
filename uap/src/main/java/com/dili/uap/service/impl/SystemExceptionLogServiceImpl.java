package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.SystemExceptionLogMapper;
import com.dili.uap.domain.SystemExceptionLog;
import com.dili.uap.service.SystemExceptionLogService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 15:27:04.
 */
@Service
public class SystemExceptionLogServiceImpl extends BaseServiceImpl<SystemExceptionLog, Long> implements SystemExceptionLogService {

    public SystemExceptionLogMapper getActualDao() {
        return (SystemExceptionLogMapper)getDao();
    }
}