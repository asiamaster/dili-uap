package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.domain.DataDictionary;
import com.dili.uap.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:39:45.
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, Long> implements DataDictionaryService {
	@Autowired DataDictionaryValueMapper dataDictionaryValueMapper;
    public DataDictionaryMapper getActualDao() {
        return (DataDictionaryMapper)getDao();
    }
    @Transactional
    @Override
    public int delete(Long id) {
    	DataDictionary dataDictionary=this.get(id);
    	
    	//根据code删除数据字典的值
    	if(dataDictionary!=null&&dataDictionary.getCode()!=null) {
    		DataDictionaryValue condition=DTOUtils.newDTO(DataDictionaryValue.class);
    		condition.setDdCode(dataDictionary.getCode());
    		this.dataDictionaryValueMapper.delete(condition);
    	}
    	
    	return super.delete(id);
    }
}