package com.dili.uap.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.rpc.fallback.UidRpcFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "dili-uid", contextId = "uidRpc", url="${UidRpc.url:}", fallbackFactory = UidRpcFallBackFactory.class)
public interface UidRpc {

	/**
	 * 获取商户编号
	 * @return
	 */
	@RequestMapping(value = "/api/bizNumber/sid53", method = RequestMethod.GET)
	BaseOutput<String> getFirmSerialNumber();

}
