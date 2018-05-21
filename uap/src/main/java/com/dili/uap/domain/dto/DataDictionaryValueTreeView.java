package com.dili.uap.domain.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataDictionaryValueTreeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7316370085458823390L;

	private Long id;
	private Long parentId;
	private String name;
	private Map<String, Object> attributes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(String name, Object value) {
		this.getAttributes().put(name, value);
	}

}
