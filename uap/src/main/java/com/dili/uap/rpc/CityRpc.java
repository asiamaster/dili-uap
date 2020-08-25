package com.dili.uap.rpc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.ss.domain.BaseOutput;

@FeignClient(name = "assets-service", contextId = "cityRpc",url = "${cityRpc.url:}")
public interface CityRpc {

	@RequestMapping(value = "/api/city/list", method = RequestMethod.POST)
	BaseOutput<List<CityDto>> list(@RequestBody CityDto query);

}
