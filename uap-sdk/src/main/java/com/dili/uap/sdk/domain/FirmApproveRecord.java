package com.dili.uap.sdk.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.dto.IDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface FirmApproveRecord extends IDTO {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime getApproveTime();

	void setApproveTime(LocalDateTime approveTime);

	Long getApproverId();

	void setApproverId(Long approverId);

	String getNotes();

	void setNotes(String notes);

	Integer getResult();

	void setResult(Integer result);
}
