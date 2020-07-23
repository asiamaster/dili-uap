package com.dili.uap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.service.UserDataAuthService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Controller
@RequestMapping("/userDataAuthApi")
public class UserDataAuthApi {
	@Autowired
	UserDataAuthService userDataAuthService;



	/**
	 * 根据userId查询UserDataAuthValue值集合接口
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listUserDataAuthValue.api", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
    BaseOutput<List<String>> listUserDataAuthValue(@RequestBody Long userId) {
		List<String> listUserDataAuthValue = userDataAuthService.listUserDataAuthValueByUserId(userId,DataAuthType.PROJECT.getCode());
		return BaseOutput.success().setData(listUserDataAuthValue);
	}

	
}