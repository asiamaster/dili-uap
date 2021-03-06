package com.dili.uap.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dili.ss.domain.BaseOutput;

@FeignClient(name = "dili-uid", contextId = "uidRpc")
public interface UidRpc {

	/**
	 * 获取商户编号
	 * @return
	 */
	@RequestMapping(value = "/api/bizNumber/sid53", method = RequestMethod.GET)
	BaseOutput<String> getFirmSerialNumber();

}
