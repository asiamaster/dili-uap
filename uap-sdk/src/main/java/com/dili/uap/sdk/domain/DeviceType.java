package com.dili.uap.sdk.domain;

public enum DeviceType {

	IOS("IOS", "IOS"), ANDROID("android", "安卓");

	private String code;
	private String name;

	private DeviceType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static DeviceType codeOf(String code) {
		for (DeviceType type : DeviceType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("未知设备类型");
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
