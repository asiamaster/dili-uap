package com.dili.uap.sdk.domain.dto;

import java.util.List;

import com.dili.ss.dto.IDTO;

public interface UserResourceQueryDto extends IDTO {

	List<String> getResourceCodes();

	void setResourceCodes(List<String> resourceCodes);

	Long getUserId();

	void setUserId(Long userId);
}
