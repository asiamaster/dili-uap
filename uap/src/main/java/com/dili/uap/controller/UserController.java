package com.dili.uap.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.*;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;
import com.dili.uap.service.DataAuthRefService;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.UserDataAuthService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.PinYinUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 16:17:38.
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Resource
	private FirmService firmService;
	@Resource
	private UserDataAuthService userDataAuthService;

	@Autowired
	private DataAuthSource dataAuthSource;

	@Autowired
	private DataAuthRefService dataAuthRefService;
	@Value("${uap.adminName:admin}")
	private String adminName;

	/**
	 * 空数据
	 * @return
	 */
	@GetMapping(value = "/empty.action")
	public @ResponseBody BaseOutput empty() {
		return BaseOutput.success();
	}

	/**
	 * 空页面
	 * @return
	 */
	@GetMapping(value = "/blank.html")
	public String blank() {
		return "user/blank";
	}

	/**
	 * 跳转到User页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@GetMapping(value = "/index.html")
	public String index(ModelMap modelMap) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			throw new NotLoginException();
		}
		String firmCode = userTicket.getFirmCode();
		Long departmentId = userTicket.getDepartmentId();
		// 用户是否属于集团
		Boolean isGroup = false;
		Firm query = DTOUtils.newInstance(Firm.class);
		if (UapConstants.GROUP_CODE.equals(firmCode)) {
			isGroup = true;
		} else {
			query.setCode(firmCode);
		}
		modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
		modelMap.put("isGroup", isGroup);
		modelMap.put("firmCode", firmCode);
		modelMap.put("departmentId", departmentId);
		return "user/index";
	}

	/**
	 * 查询User
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<User> list(User user) {
		return userService.list(user);
	}

	/**
	 * 分页查询User
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String listPage(UserDto user) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(null == userTicket){
			return BaseOutput.failure("登录超时").toString();
		}
		String userName = userTicket.getUserName();
		if(!adminName.equals(userName)){
			user.setFirmCode(userTicket.getFirmCode());
		}
		return userService.selectForEasyuiPage(user, true).toString();
	}

	/**
	 * 新增User
	 * 
	 * @param user
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "新增用户", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput insert(@Validated(AddView.class) User user) {
		String validator = (String) user.aget(IDTO.ERROR_MSG_KEY);
		if (StringUtils.isNotBlank(validator)) {
			return BaseOutput.failure(validator);
		}
		BaseOutput<Object> output = userService.save(user);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 修改User
	 * 
	 * @param user
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "修改用户", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput update(@Validated(ModifyView.class) User user) {
		String validator = (String) user.aget(IDTO.ERROR_MSG_KEY);
		if (StringUtils.isNotBlank(validator)) {
			return BaseOutput.failure(validator);
		}
		BaseOutput<Object> output = userService.save(user);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 删除User
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "删除用户", operationType = "del", systemCode = "UAP")
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput delete(Long id) {
		User user = userService.get(id);
		userService.delete(id);
		BaseOutput<Object> output = BaseOutput.success("删除成功");
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 根据角色id查询User
	 * 
	 * @param roleId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findUserByRole.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String findUserByRole(Long roleId, User user) throws Exception {
		List<User> retList = userService.findUserByRole(roleId);
		List results = ValueProviderUtils.buildDataByProvider(user, retList);
		return new EasyuiPageOutput((long) results.size(), results).toString();
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "修改用户密码", operationType = "changePassword", systemCode = "UAP")
	@RequestMapping(value = "/changePwd.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput changePwd(UserDto user) {
		Long userId = SessionContext.getSessionContext().getUserTicket().getId();
		BaseOutput output = userService.changePwd(userId, user);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 根据姓名转换邮箱信息
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/getEmailByName.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput getEmailByName(String name) {
		return BaseOutput.success().setData(PinYinUtil.getFullPinYin(name.replace(" ", "")) + UapConstants.EMAIL_POSTFIX);
	}

	/**
	 * 重置密码
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "重置用户密码", operationType = "resetPassword", systemCode = "UAP")
	@RequestMapping(value = "/resetPass.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput resetPass(Long id) {
		User user = this.userService.get(id);
		BaseOutput output = userService.resetPass(id);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output.success();
	}

	/**
	 * 用户的禁启用
	 * 
	 * @param id
	 * @param enable
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "启用用户", operationType = "enableUser", systemCode = "UAP")
	@RequestMapping(value = "/doEnable.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput doEnable(Long id, Boolean enable) {
		User user = this.userService.get(id);
		BaseOutput output = userService.upateEnable(id, enable);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 获取用户的角色信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserRolesForTree.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getUserRolesForTree(Long id) {
		List list = userService.getUserRolesForTree(id);
		return JSONArray.toJSONString(list);
	}

	/**
	 * 保存用户的角色信息
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "修改用户角色:${roleIds}", operationType = "editUserRole", systemCode = "UAP")
	@RequestMapping(value = "/saveUserRoles.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput saveUserRoles(Long userId, String[] roleIds) {
		User user = this.userService.get(userId);
		BaseOutput output = userService.saveUserRoles(userId, roleIds);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			LoggerContext.put("roleIds", JSON.toJSONString(roleIds));
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 获取当前登录用户的信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fetchLoginUserInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Object> fetchLoginUserInfo() {
		Long userId = SessionContext.getSessionContext().getUserTicket().getId();
		return userService.fetchLoginUserInfo(userId);
	}

	/**
	 * 获取数据权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserData.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Map<String, Object>> getUserData(Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录");
		}
		BaseOutput<Map<String, Object>> output = BaseOutput.success();
		Map map = Maps.newHashMap();
		// 获取数据权限的可选范围
		DataAuthRef dataAuthRef = DTOUtils.newInstance(DataAuthRef.class);
		dataAuthRef.setCode(DataAuthType.DATA_RANGE.getCode());
		List<DataAuthRef> dataAuthRefs = dataAuthRefService.list(dataAuthRef);

		DataAuthSourceService dataRangeSourceService = dataAuthSource.getDataAuthSourceServiceMap().get(dataAuthRefs.get(0).getSpringId());
		map.put("dataRange", dataRangeSourceService.listDataAuthes(dataAuthRefs.get(0).getParam()));
		// 获取用户的数据权限
		map.put("userDatas", userService.getUserDataAuthForTree(id));
		// 查询当前用户所属的权限范围
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setUserId(id);
		userDataAuth.setRefCode(DataAuthType.DATA_RANGE.getCode());
		List<UserDataAuth> userDataAuths = userDataAuthService.list(userDataAuth);
		if (CollectionUtils.isNotEmpty(userDataAuths)) {
			map.put("currDataAuth", userDataAuths.get(0).getValue());
		}
		return output.setData(map);
	}

	/**
	 * 获取项目数据权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserProjectData.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Map<String, Object>> getUserProjectData(Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录");
		}
		// 获取需要分配数据权限的用户信息
		User user = userService.get(id);
		if (null == user) {
			return BaseOutput.failure("没有该用户");
		}
		BaseOutput<Map<String, Object>> output = BaseOutput.success();
		Map map = Maps.newHashMap();
		map.put("dataProject", userService.getUserDataProjectAuthForTree(id));
		return output.setData(map);
	}

	/**
	 * 获取交易厅数据权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserTradingDataAuth.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Map<String, Object>> getUserTradingDataAuth(Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录");
		}
		// 获取需要分配数据权限的用户信息
		User user = userService.get(id);
		if (null == user) {
			return BaseOutput.failure("没有该用户");
		}
		BaseOutput<Map<String, Object>> output = BaseOutput.success();
		Map map = Maps.newHashMap();
		map.put("tradingHallDataAuth", userService.getUserTradingDataAuth(id));
		return output.setData(map);
	}

	/**
	 * 保存用户的数据权限信息
	 * 
	 * @param userId
	 * @param dataIds
	 * @param dataRange
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "修改用户数据权限:${dataAuths}", operationType = "editUserDataAuth", systemCode = "UAP")
	@RequestMapping(value = "/saveUserDatas.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput saveUserDatas(Long userId, String[] dataIds, Long dataRange) {
		User user = this.userService.get(userId);
		BaseOutput output = userService.saveUserDatas(userId, dataIds, dataRange);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			LoggerContext.put("dataAuths", JSON.toJSONString(output.getData()));
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 解锁用户
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "解锁用户", operationType = "unlockUser", systemCode = "UAP")
	@RequestMapping(value = "/unlock.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput unlock(Long id) {
		User user = this.userService.get(id);
		BaseOutput output = userService.unlock(id);
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

	/**
	 * 跳转到在线用户页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/onlineList.html", method = RequestMethod.GET)
	public String onlineList(ModelMap modelMap) {
		String firmCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
		// 用户是否属于集团
		Boolean isGroup = false;
		Firm query = DTOUtils.newInstance(Firm.class);
		if (UapConstants.GROUP_CODE.equals(firmCode)) {
			isGroup = true;
		} else {
			query.setCode(firmCode);
		}
		modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
		modelMap.put("isGroup", isGroup);
		modelMap.put("firmCode", firmCode);
		return "user/onlineList";
	}

	/**
	 * 分页查询在线用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listOnlinePage.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String listOnlinePage(UserDto user) throws Exception {
		return userService.listOnlinePage(user).toString();
	}

	/**
	 * 强制下线用户
	 * 
	 * @param id
	 * @return
	 */
	@BusinessLogger(businessType = "user_management", content = "解锁用户", operationType = "forcedOffline", systemCode = "UAP")
	@RequestMapping(value = "/forcedOffline.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput forcedOffline(Long id) {
		User user = this.userService.get(id);
		BaseOutput output = userService.forcedOffline(id, SystemType.WEB.getCode());
		if (output.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, user.getUserName());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, user.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return output;
	}

}