package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.domain.DataDictionary;
import com.dili.uap.service.DataDictionaryService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:39:45.
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, Long> implements DataDictionaryService {

    public DataDictionaryMapper getActualDao() {
        return (DataDictionaryMapper)getDao();
    }
}