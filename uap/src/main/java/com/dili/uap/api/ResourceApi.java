package com.dili.uap.api;

import io.swagger.annotations.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Resource;
import com.dili.uap.service.ResourceService;

@Api("/resource")
@Controller
@RequestMapping("/resourceApi")
public class ResourceApi {
	@Autowired
	private ResourceService resourceService;
	/**
	 * 查询权限信息名称集合
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listResourceCodeByUserId.api")
    @ResponseBody
    public BaseOutput<List<String>> listResourceCodeByUserId(@RequestBody Long userId) {
        return BaseOutput.success().setData(resourceService.listResourceCodeByUserId(userId));
    }
}
