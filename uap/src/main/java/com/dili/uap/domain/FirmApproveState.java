package com.dili.uap.domain;

public enum FirmApproveState {

	APPROVING("审批中", 1), ACCEPTED("审核通过", 2), REJECTED("审核拒绝", 3);

	private String name;
	private Integer value;

	private FirmApproveState(String name, Integer value) {
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
