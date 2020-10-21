package com.dili.uap.sdk.domain.dto;

import java.util.List;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.sdk.domain.UserPushInfo;

public interface UserPushInfoQuery extends UserPushInfo {

	@Operator(Operator.IN)
	@Column(name = "`user_id`")
	List<Long> getUserIds();

	void setUserIds(List<Long> userIds);

}
