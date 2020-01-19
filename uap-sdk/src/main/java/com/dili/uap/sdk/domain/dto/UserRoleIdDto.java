package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IBaseDomain;

public interface UserRoleIdDto extends IBaseDomain {

	Long getUserId();

	void setUserId(Long userId);

	Long getRoleId();

	void setRoleId();
}
