package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.uap.sdk.domain.DataDictionary;

import javax.persistence.Column;

public interface DataDictionaryDto extends DataDictionary{
	 @Column(name = "`name`")
	    @Like(Like.BOTH)
	    String getLikeName();
	    void setLikeName(String likeName);
}
