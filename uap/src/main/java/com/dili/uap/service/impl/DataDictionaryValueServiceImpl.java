package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryValueService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:40:13.
 */
@Service
public class DataDictionaryValueServiceImpl extends BaseServiceImpl<DataDictionaryValue, Long> implements DataDictionaryValueService {


    public DataDictionaryValueMapper getActualDao() {
        return (DataDictionaryValueMapper) getDao();
    }

}