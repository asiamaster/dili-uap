package com.dili.uap.domain;

public enum FirmApproveResult {

	ACCEPTED("通过", 1), REJECTED("拒绝", 0);

	private String name;
	private Integer value;

	private FirmApproveResult(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public static FirmApproveResult valueOf(Integer value) {
		for (FirmApproveResult result : FirmApproveResult.values()) {
			if (result.getValue().equals(value)) {
				return result;
			}
		}
		throw new IllegalArgumentException("未知的商户新增审批结果");
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

}
