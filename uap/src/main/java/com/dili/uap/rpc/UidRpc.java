package com.dili.uap.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.rpc.fallback.UidRpcFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dili-uid", contextId = "uidRpc", url="${UidRpc.url:}", fallbackFactory = UidRpcFallBackFactory.class)
public interface UidRpc {

	/**
	 * 获取商户编号
	 * @return
	 */
	@RequestMapping(value = "/api/bizNumber/sid53", method = RequestMethod.GET)
	BaseOutput<String> getFirmSerialNumber();

	/**
	 * 获取业务编号
	 * http://uid.diligrp.com:8282/api/bizNumber?type=hzsc_water
	 * @return
	 */
	@RequestMapping(value = "/api/bizNumber", method = RequestMethod.GET)
	BaseOutput<String> getBizNumber(@RequestParam String type);

}
