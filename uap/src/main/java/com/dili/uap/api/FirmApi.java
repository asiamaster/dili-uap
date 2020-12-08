package com.dili.uap.api;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.rpc.UidRpc;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.service.FirmService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/firmApi")
public class FirmApi {
	@Autowired
	FirmService firmService;

	@Autowired
	private UidRpc uidRpc;

	/**
	 * 获取商户编号(用于哨兵测试)
	 * 
	 * @return
	 */
	@GetMapping(value = "/getFirmSerialNumber.api")
	@SentinelResource(value = "getFirmSerialNumber.api", defaultFallback = "defaultFallback", blockHandler = "getFirmSerialNumberBlockHandler", entryType = EntryType.OUT)
	@ResponseBody
	public BaseOutput<String> getFirmSerialNumber() {
		BaseOutput<String> firmSerialNumber = uidRpc.getFirmSerialNumber();
		return firmSerialNumber;
	}

	/**
	 * 用于在抛出异常的时候提供 fallback 处理逻辑
	 * 
	 * @param e
	 * @return
	 */
	public BaseOutput defaultFallback(Throwable e) {
		return BaseOutput.failure("[" + e.getClass().getName() + "]异常:" + e.getMessage()).setCode(ResultCode.FLOW_LIMIT);
	}

	/**
	 * blockHandler 对应处理 BlockException 的函数名称，可选项。blockHandler 函数访问范围需要是
	 * public，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException
	 * 
	 * @param e
	 * @return
	 */
	public BaseOutput<String> getFirmSerialNumberBlockHandler(BlockException e) {
		return BaseOutput.failure("限流阻塞:" + e.getRule()).setCode(ResultCode.FLOW_LIMIT);
	}

	/**
	 * 查询公司
	 * 
	 * @param firm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Firm>> listByExample(FirmDto firm) {
		return BaseOutput.success().setData(firmService.listByExample(firm));
	}

	/**
	 * 根据编码查询公司
	 * 
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Firm> getByCode(@RequestBody String code) {
		Firm firm = DTOUtils.newInstance(Firm.class);
		firm.setCode(code);
		List<Firm> firms = firmService.list(firm);
		if (firms == null || firms.isEmpty()) {
			return BaseOutput.success().setData(Lists.newArrayList());
		}
		return BaseOutput.success().setData(firms.get(0));
	}

	/**
	 * 根据id查询公司
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getById.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<Firm> getById(@RequestBody Long id) {
		return BaseOutput.success().setData(firmService.get(id));
	}

	/**
	 * 根据id查询公司及下级公司
	 * 
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllChildrenByParentId.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<List<Firm>> getAllChildrenByParentId(@RequestBody(required = false) Long parentId) {
		return BaseOutput.success().setData(firmService.getAllChildrenByParentId(parentId));
	}

}