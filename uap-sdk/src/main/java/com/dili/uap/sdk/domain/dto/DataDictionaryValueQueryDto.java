package com.dili.uap.sdk.domain.dto;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.uap.sdk.domain.DataDictionaryValue;

public interface DataDictionaryValueQueryDto extends DataDictionaryValue {

	@Like
	@Column(name = "`name`")
	String getNameLike();

	void setNameLike(String name);
}
