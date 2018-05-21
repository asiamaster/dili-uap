package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.domain.DataDictionary;
import com.dili.uap.domain.DataDictionaryValue;
import com.dili.uap.domain.dto.DataDictionaryValueTreeView;
import com.dili.uap.service.DataDictionaryValueService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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