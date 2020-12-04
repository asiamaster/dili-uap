package com.dili.uap.domain;

public enum DataDictionaryValueState {

	ENABLED("启用", 1), DISABLED("禁用", 0);

	private String name;
	private Integer value;

	private DataDictionaryValueState(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

}
