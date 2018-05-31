package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.domain.DataDictionary;
import com.dili.uap.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryService;

import org.apache.commons.lang3.StringUtils;
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
	@Override
	public BaseOutput<Object> insertAfterCheck(DataDictionary t) {
		
		if(StringUtils.isBlank(t.getCode())){
			return BaseOutput.failure("编码不能为空");
		}
		if(StringUtils.isBlank(t.getSystemCode())){
			return BaseOutput.failure("系统编码不能为空");
		}
		DataDictionary condition=DTOUtils.newDTO(DataDictionary.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setSystemCode(StringUtils.trim(t.getSystemCode()));
		int size=this.list(condition).size();
		if(size>0) {
			return BaseOutput.failure("相同编码已经存在");
		}
		this.insertSelective(t);
		return BaseOutput.success("新增成功");
	}
	@Override
	public BaseOutput<Object> updateAfterCheck(DataDictionary t) {
		if(StringUtils.isBlank(t.getCode())){
			return BaseOutput.failure("编码不能为空");
		}
		if(StringUtils.isBlank(t.getSystemCode())){
			return BaseOutput.failure("系统编码不能为空");
		}
		DataDictionary condition=DTOUtils.newDTO(DataDictionary.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setSystemCode(StringUtils.trim(t.getSystemCode()));
		boolean exists=this.list(condition).stream().anyMatch((d)->!d.getId().equals(t.getId()));
		if(exists) {
			return BaseOutput.failure("相同编码已经存在");
		}
		this.updateSelective(t);
		return BaseOutput.success("修改成功");
	}
    
}