package com.dili.uap.sdk.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by asiamaster on 2017/7/5 0005.
 */
public class Pair<T> {
	private String name;
	private T value;

	public Pair() {
	}

	public Pair(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return this.value;
	}

	public String getValueString() {
		return this.value == null?null:this.value.toString();
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean equals(Object obj) {
		return this == obj?true:(obj instanceof Pair && StringUtils.equals(((Pair)obj).name, this.name)?this.value.equals(((Pair)obj).value):false);
	}

	public int hashCode() {
		return this.value.hashCode();
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
