package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.*;
import com.dili.uap.sdk.domain.dto.UserResourceQueryDto;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface ResourceRpc {

	@GET("/resourceApi/listResourceCodeByMenuUrl.api")
	BaseOutput<List<String>> listResourceCodeByMenuUrl(@ReqParam("url") String url, @ReqParam("userId") Long userId);

	@POST("/resourceApi/listResourceCodeByUserId")
	BaseOutput<List<String>> listResourceCodeByUserId(@VOBody Long id);

	@POST("/resourceApi/listResourceCodesByUserId")
	BaseOutput<List<String>> listResourceCodesByUserId(@VOBody UserResourceQueryDto dto);
}
