package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.GET;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface ResourceRpc {

	@GET("/resourceApi/listResourceCodeByMenuUrl.api")
	BaseOutput<List<String>> listResourceCodeByMenuUrl(@ReqParam("url") String url, @ReqParam("userId")Long userId);

}
