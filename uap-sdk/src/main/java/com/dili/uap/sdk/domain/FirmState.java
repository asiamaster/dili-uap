package com.dili.uap.sdk.domain;

public enum FirmState {

	ENABLED("已启用", 1), DISABLED("已禁用", 0);

	private String name;
	private Integer value;

	private FirmState(String name, Integer value) {
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
