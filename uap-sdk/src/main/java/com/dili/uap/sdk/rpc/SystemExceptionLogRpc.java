package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.SystemExceptionLog;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface SystemExceptionLogRpc {

	@POST("/systemExceptionLog/insert.api")
	BaseOutput insert(@VOBody SystemExceptionLog systemExceptionLog);

}
