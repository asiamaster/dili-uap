package com.dili.uap.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Resource;
import com.dili.uap.service.ResourceService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-12-30 16:56:50.
 */
@Api("/resourceApi")
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
	@ApiOperation(value = "查询权限信息名称集合接口", notes = "根据userId查询resource接口，返回List")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "user的id", required = true, dataType = "long") })
	@RequestMapping(value = "/listResourceCodeByUserId.api")
    @ResponseBody
    public BaseOutput<List<String>> listResourceCodeByUserId(@RequestBody Long userId) {
        return BaseOutput.success().setData(resourceService.listResourceCodeByUserId(userId));
    }
}