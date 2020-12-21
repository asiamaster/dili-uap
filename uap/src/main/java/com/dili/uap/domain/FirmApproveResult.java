package com.dili.uap.domain;

public enum FirmApproveResult {

	ACCEPTED("通过", 1), REJECTED("拒绝", 0);

	private String name;
	private Integer value;

	private FirmApproveResult(String name, Integer value) {
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
