package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:40:13.
 */
public interface DataDictionaryValueService extends BaseService<DataDictionaryValue, Long> {

    /**
     * 根据数据字典编码获取数据字典值
     * @param code
     * @return
     */
    List<DataDictionaryValue> listDictionaryValueByCode(String code);
    
    /** 新增数据字典值
	 * @param t 字典值数据
	 * @return
	 */
	public BaseOutput<Object> insertAfterCheck(DataDictionaryValue t) ;
	
	/** 修改数据字典值
	 * @param t 字典值数据
	 * @return
	 */
	public BaseOutput<Object> updateAfterCheck(DataDictionaryValue t) ;

}