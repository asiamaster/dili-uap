package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.SystemMapper;
import com.dili.uap.domain.System;
import com.dili.uap.service.SystemService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:24:56.
 */
@Service
public class SystemServiceImpl extends BaseServiceImpl<System, Long> implements SystemService {

    public SystemMapper getActualDao() {
        return (SystemMapper)getDao();
    }
}