package com.dili.uap.rpc;

import com.dili.assets.sdk.dto.BankUnionInfoDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "assets-service", contextId = "bankUnionRpc", url = "${bankUnionRpc.url:}")
public interface BankUnionInfoRpc {

	@RequestMapping(value = "/api/bankUnionInfo/list", method = RequestMethod.POST)
	BaseOutput<List<BankUnionInfoDto>> list(@RequestBody BankUnionInfoDto query);
}
