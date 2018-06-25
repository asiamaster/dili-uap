package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.service.DataDictionaryValueService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public List<DataDictionaryValue> listDictionaryValueByCode(String code) {
        return null;
    }
    @Override
	public BaseOutput<Object> insertAfterCheck(DataDictionaryValue t) {
		
		if(StringUtils.isBlank(t.getCode())){
			return BaseOutput.failure("编码不能为空");
		}
		if(StringUtils.isBlank(t.getDdCode())){
			return BaseOutput.failure("系统编码不能为空");
		}
		DataDictionaryValue condition=DTOUtils.newDTO(DataDictionaryValue.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setDdCode(StringUtils.trim(t.getDdCode()));
		int size=this.list(condition).size();
		if(size>0) {
			return BaseOutput.failure("相同编码已经存在");
		}
		this.insertSelective(t);
		return BaseOutput.success("新增成功");
	}
	@Override
	public BaseOutput<Object> updateAfterCheck(DataDictionaryValue t) {
		if(StringUtils.isBlank(t.getCode())){
			return BaseOutput.failure("编码不能为空");
		}
		if(StringUtils.isBlank(t.getDdCode())){
			return BaseOutput.failure("系统编码不能为空");
		}
		DataDictionaryValue condition=DTOUtils.newDTO(DataDictionaryValue.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setDdCode(StringUtils.trim(t.getDdCode()));
		boolean exists=this.list(condition).stream().anyMatch((d)->!d.getId().equals(t.getId()));
		if(exists) {
			return BaseOutput.failure("相同编码已经存在");
		}
		this.updateSelective(t);
		return BaseOutput.success("修改成功");
	}
}