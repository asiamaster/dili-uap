package com.dili.uap.domain.dto;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.uap.domain.DataDictionary;

public interface DataDictionaryDto extends DataDictionary{
	 @Column(name = "`name`")
	    @Like(Like.BOTH)
	    String getLikeName();
	    void setLikeName(String likeName);
}
