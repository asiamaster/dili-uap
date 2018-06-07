package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.domain.UserDataAuth;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.service.DataAuthService;
import com.dili.uap.service.UserDataAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据权限Api
 */
@Api("/dataAuthApi")
@Controller
@RequestMapping("/dataAuthApi")
public class DataAuthApi {
	@Autowired
	private DataAuthService dataAuthService;

	@Autowired
	private UserDataAuthService userDataAuthService;

	@Autowired
	private DataAuthManager dataAuthManager;

	@ApiOperation("刷新DataAuth缓存")
	@RequestMapping(value = "/refreshAuthData.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput refreshAuthData(String json){
		JSONObject jo = JSON.parseObject(json);
		dataAuthManager.listUserDataAuthesByType(jo.getLong("userId"), jo.getString("type"));
		return BaseOutput.success("刷新缓存成功");
	}

	@ApiOperation("新增DataAuth")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataAuth", paramType = "form", value = "DataAuth的form信息", required = true, dataType = "DataAuth") })
	@RequestMapping(value = "/addDataAuth.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput addDataAuth(DataAuth dataAuth) {
		dataAuthService.insertSelective(dataAuth);
		return BaseOutput.success("新增成功");
	}

	@ApiOperation("根据dataId和type修改DataAuth的名称")
	@ApiImplicitParams({ @ApiImplicitParam(name = "DataAuth", paramType = "form", value = "DataAuth的form信息", required = true, dataType = "DataAuth") })
	@RequestMapping(value = "/updateDataAuth.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput updateDataAuth(DataAuth dataAuth) {
//		DataAuth condition = DTOUtils.newDTO(DataAuth.class);
//		condition.setDataId(dataAuth.getDataId());
//		condition.setType(dataAuth.getType());
//		List<DataAuth> dataAuths = dataAuthService.list(condition);
//		if (ListUtils.emptyIfNull(dataAuths).size() != 1) {
//			return BaseOutput.failure("修改失败，dataId:" + dataAuth.getDataId() + "和type:" + dataAuth.getType() + ",不能找到唯一的数据权限");
//		}
//		dataAuth.setId(dataAuths.get(0).getId());
//		dataAuthService.updateSelective(dataAuth);
		DataAuth dataAuthCondition = DTOUtils.newDTO(DataAuth.class);
		dataAuthCondition.setDataId(dataAuth.getDataId());
		dataAuthCondition.setType(dataAuth.getType());
		dataAuthService.updateSelectiveByExample(dataAuth, dataAuthCondition);
		return BaseOutput.success("修改成功");
	}

	@ApiOperation("根据dataId和type删除DataAuth")
	@ApiImplicitParams({ @ApiImplicitParam(name = "dataId", paramType = "form", value = "dataId", required = true, dataType = "DataAuth"),
			@ApiImplicitParam(name = "type", paramType = "form", value = "type", required = true, dataType = "string") })
	@RequestMapping(value = "/deleteDataAuth.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput deleteDataAuth(DataAuth dataAuth) {
		DataAuth dataAuthCondition = DTOUtils.newDTO(DataAuth.class);
		dataAuthCondition.setDataId(dataAuth.getDataId());
		dataAuthCondition.setType(dataAuth.getType());
		List<DataAuth> list = dataAuthService.list(dataAuthCondition);
		if (ListUtils.emptyIfNull(list).size() != 1) {
			return BaseOutput.failure("删除失败，dataId:" + dataAuth.getDataId() + "和type:" + dataAuth.getType() + ",不能找到唯一的数据权限");
		}
		dataAuthService.delete(list.get(0).getId());
		return BaseOutput.success("删除成功");
	}

	@ApiOperation("添加用户数据权关系")
	@ApiImplicitParams({ @ApiImplicitParam(name = "json", paramType = "form", value = "type,dataId和userId", required = true, dataType = "String") })
	@ResponseBody
	@RequestMapping(value = "/addUserDataAuth.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> addUserDataAuth(@RequestBody String json) {
		// type
		JSONObject jo = JSON.parseObject(json);
		DataAuth dataAuth = DTOUtils.newDTO(DataAuth.class);
		dataAuth.setType(jo.getString("type"));
		dataAuth.setDataId(jo.getString("dataId"));
		List<DataAuth> dataAuths = dataAuthService.list(dataAuth);
		if (ListUtils.emptyIfNull(dataAuths).size() != 1) {
			return BaseOutput.failure("新增失败，dataId:" + jo.getString("dataId") + "和type:" + jo.getString("type") + ",不能找到唯一的数据权限");
		}
		UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(jo.getLong("userId"));
		userDataAuth.setDataAuthId(dataAuths.get(0).getId());
		if (userDataAuthService.list(userDataAuth).isEmpty()) {
			userDataAuthService.insertSelective(userDataAuth);
		}
		return BaseOutput.success("添加用户数据权成功");
	}

	@ApiOperation("删除用户数据权关系")
	@ApiImplicitParams({ @ApiImplicitParam(name = "json", paramType = "form", value = "type,dataId和userId", required = true, dataType = "String") })
	@ResponseBody
	@RequestMapping(value = "/deleteUserDataAuth.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> deleteUserDataAuth(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		DataAuth dataAuth = DTOUtils.newDTO(DataAuth.class);
		dataAuth.setType(jo.getString("type"));
		dataAuth.setDataId(jo.getString("dataId"));
		List<DataAuth> dataAuths = dataAuthService.list(dataAuth);
		if (ListUtils.emptyIfNull(dataAuths).size() != 1) {
			return BaseOutput.failure("删除失败，dataId:" + jo.getString("dataId") + "和type:" + jo.getString("type") + ",不能找到唯一的数据权限");
		}
		UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(jo.getLong("userId"));
		userDataAuth.setDataAuthId(dataAuths.get(0).getId());
		userDataAuthService.delete(userDataAuth);
		return BaseOutput.success("删除用户数据权成功");
	}
}