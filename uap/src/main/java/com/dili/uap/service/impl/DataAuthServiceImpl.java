package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.DataAuthMapper;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.service.DataAuthService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-23 11:29:55.
 */
@Service
public class DataAuthServiceImpl extends BaseServiceImpl<DataAuth, Long> implements DataAuthService {

    public DataAuthMapper getActualDao() {
        return (DataAuthMapper)getDao();
    }
}