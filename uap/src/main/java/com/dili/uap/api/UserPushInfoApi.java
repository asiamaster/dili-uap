package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserPushInfo;
import com.dili.uap.sdk.domain.dto.UserPushInfoQuery;
import com.dili.uap.service.UserPushInfoService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/userPushInfoApi")
public class UserPushInfoApi {
	@Autowired
	private UserPushInfoService userPushInfoService;

	/**
	 * 查询User实体接口
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/listByExample.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<?> get(UserPushInfoQuery query) {
		List<UserPushInfo> list = this.userPushInfoService.listByExample(query);
		return BaseOutput.successData(list);
	}
}