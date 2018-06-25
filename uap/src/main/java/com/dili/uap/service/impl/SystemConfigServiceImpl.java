package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.SystemConfigMapper;
import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.service.SystemConfigService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 14:34:21.
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements SystemConfigService {

    public SystemConfigMapper getActualDao() {
        return (SystemConfigMapper)getDao();
    }
}