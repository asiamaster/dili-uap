package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.DataAuthRefMapper;
import com.dili.uap.domain.DataAuthRef;
import com.dili.uap.service.DataAuthRefService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-09 11:26:38.
 */
@Service
public class DataAuthRefServiceImpl extends BaseServiceImpl<DataAuthRef, Long> implements DataAuthRefService {

    public DataAuthRefMapper getActualDao() {
        return (DataAuthRefMapper)getDao();
    }
}