package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.domain.Firm;
import com.dili.uap.service.FirmService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-23 14:31:07.
 */
@Service
public class FirmServiceImpl extends BaseServiceImpl<Firm, Long> implements FirmService {

    public FirmMapper getActualDao() {
        return (FirmMapper)getDao();
    }
}