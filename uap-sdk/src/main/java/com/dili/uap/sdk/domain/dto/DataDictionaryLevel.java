package com.dili.uap.sdk.domain.dto;

/**
 * 数据字典级别
 * 
 * @author jiang
 *
 */
public enum DataDictionaryLevel {

	GROUP(0), BUSINESS(1);

	private DataDictionaryLevel(Integer value) {
		this.value = value;
	}

	private Integer value;

	public Integer getValue() {
		return value;
	}

}
