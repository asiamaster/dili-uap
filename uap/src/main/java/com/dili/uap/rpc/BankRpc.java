package com.dili.uap.rpc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dili.assets.sdk.dto.BankDto;
import com.dili.ss.domain.BaseOutput;

@FeignClient(name = "assets-service", contextId = "bankRpc", url = "${bankRpc.url:}")
public interface BankRpc {

	@RequestMapping(value = "/api/bank/list", method = RequestMethod.POST)
	BaseOutput<List<BankDto>> list(@RequestBody BankDto query);
}
