package com.dili.uap.sdk.domain;

/**
 * 部门类型
 * 
 * @author jiang
 *
 */
public enum DepartmentType {

	BUSINESS("业务部门", 1), ADMINISTRATIVE("行政部门", 2);

	private String name;
	private Integer value;

	private DepartmentType(String name, Integer value) {
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
