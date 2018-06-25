package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionary;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 10:39:45.
 */
public interface DataDictionaryService extends BaseService<DataDictionary, Long> {
	
	/** 新增数据字典
	 * @param t 字典数据
	 * @return
	 */
	public BaseOutput<Object> insertAfterCheck(DataDictionary t) ;
	
	/** 修改数据字典
	 * @param t 字典数据
	 * @return
	 */
	public BaseOutput<Object> updateAfterCheck(DataDictionary t) ;
	
	
	/** 删除数据字典
	 * @param id 字典数据ID
	 * @return
	 */
	public BaseOutput<Object> deleteAfterCheck(Long id) ;
}