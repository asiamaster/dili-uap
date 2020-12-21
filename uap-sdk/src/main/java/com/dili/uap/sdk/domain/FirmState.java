package com.dili.uap.sdk.domain;

public enum FirmState {

	ENABLED("开通", 1), DISABLED("关闭", 0), UNREVIEWED("未审核", 3), APPROVING("审批中", 4);

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
