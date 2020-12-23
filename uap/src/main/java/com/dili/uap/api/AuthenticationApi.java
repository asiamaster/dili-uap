package com.dili.uap.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.RSAUtils;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.dto.LoginDto;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.manager.DataAuthManager;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.DataAuthRef;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.UserPushInfo;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.DataAuthRedis;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserSystemRedis;
import com.dili.uap.sdk.rpc.SystemConfigRpc;
import com.dili.uap.service.*;
import com.dili.uap.utils.WebUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by asiam on 2018/6/7 0007.
 */
@Controller
@RequestMapping("/authenticationApi")
public class AuthenticationApi {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRedis userRedis;

	@Autowired
	private UserSystemRedis userSystemRedis;

	@Autowired
	private DataAuthRedis dataAuthRedis;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private DataAuthManager dataAuthManager;

	@Autowired
	private DataAuthSource dataAuthSource;

	@Autowired
	private DataAuthRefService dataAuthRefService;

	@Value("${rsaPrivateKey:}")
	private String rsaPrivateKey;

	@Autowired
	private SystemConfigRpc systemConfigRpc;
	@Autowired
	private UserPushInfoService userPushInfoService;
	@Autowired
	private TerminalBindingService terminalBindingService;

	/**
	 * 统一授权登录，返回登录用户信息LoginResult
	 * 
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput login(@RequestBody String json, HttpServletRequest request) {
		try {
			json = decryptRSA(json);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
		JSONObject jsonObject = JSONObject.parseObject(json);
		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
		loginDto.setUserName(jsonObject.getString("userName"));
		loginDto.setPassword(jsonObject.getString("password"));
		// 设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		// 设置ip和hosts,用于记录登录日志
		loginDto.setIp(WebUtil.getRemoteIP(request));
		loginDto.setHost(request.getRemoteHost());
		return loginService.login(loginDto);
	}

	/**
	 * 统一授权登录，返回登录用户信息LoginResult
	 * 
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginFromApp.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput loginFromApp(@RequestBody String json, HttpServletRequest request) {
//		try {
//			json = decryptRSA(json);
//		} catch (Exception e) {
//			return BaseOutput.failure(e.getMessage());
//		}
		JSONObject jsonObject = JSONObject.parseObject(json);
		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
		loginDto.setUserName(jsonObject.getString("userName"));
		try {
			loginDto.setPassword(decryptRSA(jsonObject.getString("password")));
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
		loginDto.setPushId(jsonObject.getString("pushId"));
		loginDto.setDeviceType(jsonObject.getString("deviceType") != null ? jsonObject.getString("deviceType").toLowerCase() : null);
		// 设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		// 设置ip和hosts,用于记录登录日志
		loginDto.setIp(WebUtil.getRemoteIP(request));
		loginDto.setHost(request.getRemoteHost());
		BaseOutput<LoginResult> output = loginService.loginFromApp(loginDto);
		if (!output.isSuccess()) {
			return output;
		}
		UserTicket userTicket = this.userRedis.getTokenUser(output.getData().getToken());
		Map param = new HashMap(2);
		param.put("userId", output.getData().getUser().getId());
		return BaseOutput.successData(new HashMap<String, Object>() {
			{
				put("token", output.getData().getToken());
				put("user", userTicket);
			}
		});
	}

	/**
	 * 绑定终端号
	 * 
	 * @param token      登录token
	 * @param terminalId 终端号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bindTerminal.api")
	public BaseOutput<Object> bindTerminal(@RequestParam String token, @RequestParam String terminalId) {
		return this.terminalBindingService.bindByToken(token, terminalId);
	}

	/**
	 * 用户登录验证，返回新的sessionId
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/validate.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput validate(@RequestBody String json) {
		try {
			json = decryptRSA(json);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
		BaseOutput output = systemConfigRpc.list(null);
		JSONObject jsonObject = JSONObject.parseObject(json);
		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
		loginDto.setUserName(jsonObject.getString("userName"));
		loginDto.setPassword(jsonObject.getString("password"));
		return loginService.validateSaveSession(loginDto);
	}

	/**
	 * 用户登录验证，返回新的sessionId
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/validateAndGetUserInfo.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput validateAndGetUserInfo(@RequestBody String json) {
		BaseOutput output = this.validate(json);
		if (!output.isSuccess()) {
			return output;
		}
		UserTicket userTicket = this.userRedis.getUser(output.getData().toString());
		return BaseOutput.successData(new HashMap<String, Object>() {
			{
				put("sessionId", output.getData().toString());
				put("userTicket", userTicket);
			}
		});
	}

	/**
	 * 根据sessionId判断用户是否登录 无返回信息
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/authentication.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput authentication(@RequestBody String json) {
		try {
			json = decryptRSA(json);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
		String sessionId = getSessionIdByJson(json);
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		return userRedis.getSessionUserId(sessionId) == null ? BaseOutput.failure("未登录").setCode(ResultCode.NOT_AUTH_ERROR) : BaseOutput.success("已登录");
	}

	/**
	 * 移动端登出
	 * 
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appLoginout.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput appLoginout(@RequestBody String json, HttpServletRequest request) {
		String sessionId = getSessionIdByJson(json);
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		UserTicket userTicket = this.userRedis.getUser(sessionId);
		if (userTicket == null) {
			return BaseOutput.failure("未获取到登录用户信息");
		}
		UserPushInfo condition = DTOUtils.newInstance(UserPushInfo.class);
		condition.setUserId(userTicket.getId());
		this.userPushInfoService.deleteByExample(condition);
		userService.logout(sessionId);
		return BaseOutput.success("登出成功");
	}

	/**
	 * 统一授权登出
	 * 
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginout.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput loginout(@RequestBody String json, HttpServletRequest request) {
		String sessionId = getSessionIdByJson(json);
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		userService.logout(sessionId);
		return BaseOutput.success("登出成功");
	}

	/**
	 * 根据sessionId取用户名
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/getUserNameBySessionId.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput getUserNameBySessionId(@RequestBody String json) {
		String sessionId = getSessionIdByJson(json);
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		String userName = userRedis.getUserNameBySessionId(sessionId);
		return userName == null ? BaseOutput.failure("未登录").setCode(ResultCode.NOT_AUTH_ERROR) : BaseOutput.success(userName);
	}

	/**
	 * 根据sessionId获取系统权限列表，如果未登录将返回空
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listSystems.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput<List<Systems>> listSystems(@RequestBody String json) {
		String sessionId = getSessionIdByJson(json);
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		if (userId == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		return BaseOutput.success("调用成功").setData(userSystemRedis.getRedisUserSystems(userId));
	}

	/**
	 * 获取菜单权限列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listMenus.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Object> listMenus(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sessionId = jsonObject.getString("sessionId");
		String systemId = jsonObject.getString("systemId");
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		if (userId == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		Map param = new HashMap(2);
		param.put("userId", userId);
		param.put("systemId", systemId);
		return BaseOutput.success("调用成功").setData(this.menuMapper.listClientMenus(param));
	}

	/**
	 * 获取资源权限列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listResources.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput<List<Resource>> listResources(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sessionId = jsonObject.getString("sessionId");
		Long systemId = jsonObject.getLong("systemId");
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		if (userId == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		if (systemId == null) {
			return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserId(userId));
		}
		return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserIdAndSystemId(userId, systemId));
	}

	/**
	 * 获取数据权限列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listDataAuthes.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput<List<Map>> listDataAuthes(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sessionId = jsonObject.getString("sessionId");
		String refCode = jsonObject.getString("refCode");
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		if (userId == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		// 返回UserDataAuth列表
		return BaseOutput.success("调用成功").setData(dataAuthManager.listUserDataAuthesByRefCode(userId, refCode));
	}

	/**
	 * 获取数据权限详情列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listDataAuthDetails.api", method = { RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Map<String, Map>> listDataAuthDetails(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sessionId = jsonObject.getString("sessionId");
		String refCode = jsonObject.getString("refCode");
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		if (StringUtils.isBlank(refCode)) {
			return BaseOutput.failure("refCode不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		if (userId == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		// 查询数据权限引用dataAuthRef
		DataAuthRef dataAuthRef = DTOUtils.newInstance(DataAuthRef.class);
		dataAuthRef.setCode(refCode);
		List<DataAuthRef> dataAuthRefs = this.dataAuthRefService.list(dataAuthRef);
		if (CollectionUtils.isEmpty(dataAuthRefs)) {
			return BaseOutput.failure("数据权限引用不存在");
		}
		// 从Redis获取用户有权限的UserDataAuth列表
		List<Map> userDataAuthes = this.dataAuthRedis.dataAuth(refCode, userId);
		if (CollectionUtils.isEmpty(userDataAuthes)) {
			return BaseOutput.success("调用成功").setData(userDataAuthes);
		}
		List values = userDataAuthes.parallelStream().map(t -> t.get("value")).collect(Collectors.toList());
		Map<String, Map> dataAuthMap = dataAuthSource.getDataAuthSourceServiceMap().get(dataAuthRefs.get(0).getSpringId()).bindDataAuthes(dataAuthRefs.get(0).getParam(), values);
		// 返回UserDataAuth列表
		return BaseOutput.success("调用成功").setData(dataAuthMap);
	}

	/**
	 * 修改密码
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/changePwd.api", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput changePwd(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sessionId = jsonObject.getString("sessionId");
		String oldPassword = jsonObject.getString("oldPassword");
		String newPassword = jsonObject.getString("newPassword");
		String confirmPassword = jsonObject.getString("confirmPassword");
		if (StringUtils.isBlank(sessionId)) {
			return BaseOutput.failure("会话id不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		Long userId = userRedis.getSessionUserId(sessionId);
		UserDto userDto = DTOUtils.newInstance(UserDto.class);
		userDto.setId(userId);
		userDto.setNewPassword(newPassword);
		userDto.setConfirmPassword(confirmPassword);
		userDto.setOldPassword(oldPassword);
		return userService.changePwd(userId, userDto);
	}

	/**
	 * 从json中获取sessionId
	 * 
	 * @param json
	 * @return
	 */
	private String getSessionIdByJson(String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		return jsonObject.getString("sessionId");
	}

	/**
	 * RSA解密
	 * 
	 * @param code
	 * @return
	 */
	private String decryptRSA(String code) throws Exception {
		return new String(RSAUtils.decryptByPrivateKey(Base64.decodeBase64(code), Base64.decodeBase64(rsaPrivateKey)));
	}

	/**
	 * 登录密钥计算 admin:asdf1234 ->
	 * PgSgiZ4DO+JLkNRapTG1aMu3b9s47DiNFOhnF0a0OrpQmDip51uVTG7HKUg9EsFP3VLvPeAhUKhw/irwOB38/Q==
	 * jt_test:asdf1234 ->
	 * Ac4uyQRdP9NNVMLSGpKbyn1E6j5znTa65IoyWvUjLBeYIW3i9vuXAef3Mnz0qkfLAGIOh/jhnviCTi2UxzanFg==
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main1(String[] args) throws Exception {
		// 私钥
		String privateStr = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAtmEBC5xciJySRAqchSYQR5tnEzsKO/dK0Fg1dVBKKPPwETD5HrQqcDPegRwoiZm8ASpVA2MKZd0iBHFU/M7wNQIDAQABAkEAtK25OWV4jqZ+iQXyNj6VVjtwjC6rXukIpwscOtKGBbalCLgRAs8Q0ZePqe9Duj3/vE8/ZZuTXjSlsJlVSCp/aQIhAPdo8I2aLJrkm/om/CtUHvlW1TCw14eP28zvChQzIx4zAiEAvLYMMVcHD7pe+Xj0hfnc+rmai/64zcjP4VpknqHI//cCIF8bRwWYE7eDU/ZokB1z2+hLme56vI+PHJZ9+Wjkc4aDAiBdJ0Rnir06n1ZIsdOK2yehQMOwfaH+OzWa2YM350cQSwIgOscoD26vCWCF3Q35Tn16RgRYSSyk28s+uqZs1Ld4PvU=";
		privateStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKA3uR1rkvjO05Sq6geO5tAgTKoGmOlph6r4MEpcDck05mwyMbAsZWp8LBCDcccAZNpafGynmCOaHS1LOIkWBB/a7jlOoDZQ5s6+SVGFtavnpChM2yObvbBmhI6VTNbfEeL/o+hwd3L/tsw6IN/p+ZiEUFl1GV5fq0ICnvDoOx1tAgMBAAECgYBLqmVjbpWHqe4krR8/mI6LRmXOerUmru8ioHn19EmSd8hG2uG6iQ0QYDpTRjCqwhXfRZKzoebpEXSsGnVF0L77KMgrsSQwBS7Q023gRK9xZgdPmOq7+wXizIEyahwElFFFg8QKJBH0GsVnOKhmsFjyM+tUY2mMcseFsXBXHBs14QJBANZrJRNV8fBlNs6VsW8GtEvRj0n5XA06SM/uE4XItaSM6Wa3150ssehrfmQyMudF5Xr4/abH6HO3G+Ovg/amBMkCQQC/ScOvRvtsJ0GM8D88uY23uT3R8FqnEkghPsYzjMaVGRAWFa24PIBr1mh5/DZ6nJXS7eYozhMjA9GdbO1h6xmFAkBfQmLKYFiIcK8UwLR/mv7m4EdEmiAnUEmg9yh9O1pXrLLVC8Ai+ARiOb+BTDwJO6hkJdKrEg1Xu3YMhaGfJsrhAkEAq4QtlN0VlS1Bxmiomv9ZhfHv720PK5zl1gFeYFyKtqepV9QKVxbQo2C1fyNZiekbIe00IURdvlivO/OuiczurQJAC6bMIu+Z4v7XkdzAdVvA/azKjAGQrQOCYGvwZPsqxOd65FcX8hmSwuIAOXo1Pjl7GeW/P2x0Cis667e5mn7xrg==";
		java.lang.System.out.println("java私钥:" + privateStr);
		byte[] privateBytes = Base64.decodeBase64(privateStr);
		// 公钥
		String publicStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALZhAQucXIickkQKnIUmEEebZxM7Cjv3StBYNXVQSijz8BEw+R60KnAz3oEcKImZvAEqVQNjCmXdIgRxVPzO8DUCAwEAAQ==";
		publicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgN7kda5L4ztOUquoHjubQIEyqBpjpaYeq+DBKXA3JNOZsMjGwLGVqfCwQg3HHAGTaWnxsp5gjmh0tSziJFgQf2u45TqA2UObOvklRhbWr56QoTNsjm72wZoSOlUzW3xHi/6PocHdy/7bMOiDf6fmYhFBZdRleX6tCAp7w6DsdbQIDAQAB";
		java.lang.System.out.println("java公钥:" + publicStr);
		byte[] publicBytes = Base64.decodeBase64(publicStr);
		String content = "{userName:\"jt_test\", password:\"asdf1234\"}";

		byte[] encryptByPublic = RSAUtils.encryptByPublicKey(content.getBytes(), publicBytes);
		java.lang.System.out.println("===========甲方使用公钥对数据进行加密==============");
		java.lang.System.out.println("加密后的数据：" + Base64.encodeBase64String(encryptByPublic));
		// 加密后的数据：pDm5Ge+2N16d7PbyeucjK7QYq7bWWqbZ7WiIv6706gLwuwyG088/AMTlloeDihSkQkP4sRyxS0ivY9UACNVVdg==
		java.lang.System.out.println("===========乙方使用私钥对数据进行解密==============");
		// 甲方使用私钥对数据进行解密
		byte[] decryptByPrivate = RSAUtils.decryptByPrivateKey(encryptByPublic, privateBytes);
		java.lang.System.out.println("乙方解密后的数据：" + new String(decryptByPrivate));
	}
}