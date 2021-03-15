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
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.ClientMenuDto;
import com.dili.uap.sdk.service.AuthService;
import com.dili.uap.sdk.service.redis.DataAuthRedis;
import com.dili.uap.sdk.service.redis.UserRedis;
import com.dili.uap.sdk.service.redis.UserSystemRedis;
import com.dili.uap.service.*;
import com.dili.uap.utils.WebUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
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
 * 鉴权接口
 * Created by asiam on 2018/6/7 0007.
 */
@Controller
@RequestMapping("/api/authenticationApi")
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
	private AuthService authService;
	@Autowired
	private UserPushInfoService userPushInfoService;
	@Autowired
	private TerminalBindingService terminalBindingService;

	/**
	 * 统一授权登录WEB，返回登录用户信息LoginResult
	 * 
	 * @param json key: userName, password
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/loginWeb.api")
	@ResponseBody
	public BaseOutput<LoginResult> loginWeb(@RequestBody String json, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = JSONObject.parseObject(json);
		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
		loginDto.setUserName(jsonObject.getString("userName"));
		loginDto.setPassword(decryptRSA(jsonObject.getString("password")));
		// 设置登录后需要返回的上一页URL,用于记录登录地址到Cookie
		loginDto.setLoginPath(WebUtil.fetchReferer(request));
		// 设置ip和hosts,用于记录登录日志
		loginDto.setIp(WebUtil.getRemoteIP(request));
		loginDto.setHost(request.getRemoteHost());
		return loginService.loginWeb(loginDto);
	}

	/**
	 * 统一授权登录APP，返回登录用户信息LoginResult
	 * 
	 * @param json key: userName, password, pushId, deviceType
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/loginApp.api")
	@ResponseBody
	public BaseOutput<LoginResult> loginApp(@RequestBody String json, HttpServletRequest request) {
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
		return loginService.loginApp(loginDto);
	}

	/**
	 * 绑定终端号
	 * 
	 * @param json key: accessToken, refreshToken, terminalId
	 * @return
	 */
	@ResponseBody
	@PostMapping("/bindTerminal.api")
	public BaseOutput<Object> bindTerminal(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		return this.terminalBindingService.bindByToken(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"), jsonObject.getString("terminalId"));
	}

	/**
	 * 用户登录验证，返回LoginResult
	 * 
	 * @param json userName和password
	 * @return
	 */
//	@PostMapping(value = "/validate.api")
//	@ResponseBody
//	public BaseOutput<LoginResult> validate(@RequestBody String json) {
//		try {
//			json = decryptRSA(json);
//		} catch (Exception e) {
//			return BaseOutput.failure(e.getMessage());
//		}
//		JSONObject jsonObject = JSONObject.parseObject(json);
//		LoginDto loginDto = DTOUtils.newInstance(LoginDto.class);
//		loginDto.setUserName(jsonObject.getString("userName"));
//		loginDto.setPassword(jsonObject.getString("password"));
//		return loginService.loginWeb(loginDto);
//	}

	/**
	 * 根据token判断用户是否登录 返回UserTicket
	 * @param json key: accessToken, refreshToken
	 * @return
	 */
	@PostMapping(value = "/authentication.api")
	@ResponseBody
	public BaseOutput<UserTicket> authentication(@RequestBody String json) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(json);
			UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
			if(userTicket == null){
				return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
			}
			return BaseOutput.successData(userTicket);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}

	/**
	 * 统一授权登出
	 * 
	 * @param refreshToken
	 * @return
	 */
	@RequestMapping(value = "/logout.api", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput logout(@RequestParam String refreshToken) {
		try {
			userService.logout(refreshToken);
			return BaseOutput.success("登出成功");
		} catch (Exception e) {
			return BaseOutput.failure("登出失败:"+e.getMessage());
		}
	}

	/**
	 * 根据token获取系统权限列表，如果未登录将返回BaseOutput.failure
	 * @param json key: accessToken, refreshToken
	 * @return
	 */
	@PostMapping(value = "/listSystems.api")
	@ResponseBody
	public BaseOutput<List<Systems>> listSystems(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		return BaseOutput.success("调用成功").setData(userSystemRedis.getRedisUserSystems(userTicket.getId(), userTicket.getSystemType()));
	}

	/**
	 * 根据token获取用户有权限的系统和菜单列表
	 * @param json key: accessToken, refreshToken, systemId(非必填)
	 * @return
	 */
	@PostMapping(value = "/listSystemAndMenus.api")
	@ResponseBody
	public BaseOutput<ClientMenuDto> listSystemAndMenus(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		Map param = new HashMap(4);
		param.put("userId", userTicket.getId());
		param.put("systemId", jsonObject.getString("systemId"));
		param.put("systemType", userTicket.getSystemType());
		return BaseOutput.success("调用成功").setData(this.menuMapper.listSystemAndMenus(param));
	}

	/**
	 * 根据token获取用户有权限的资源列表
	 * @param json key: accessToken, refreshToken, systemId(非必填)
	 * @return
	 */
	@PostMapping(value = "/listResources.api")
	@ResponseBody
	public BaseOutput<List<Resource>> listResources(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		if (jsonObject.getString("systemId") == null) {
			return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserId(userTicket.getId(), userTicket.getSystemType()));
		}
		return BaseOutput.success("调用成功").setData(this.resourceMapper.listByUserIdAndSystemId(userTicket.getId(), jsonObject.getLong("systemId")));
	}

	/**
	 * 获取数据权限列表
	 * @param json key: accessToken, refreshToken, refCode
	 * @return
	 */
	@PostMapping(value = "/listDataAuthes.api")
	@ResponseBody
	public BaseOutput<List<UserDataAuth>> listDataAuthes(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		//返回UserDataAuth列表
		return BaseOutput.success("调用成功").setData(dataAuthManager.listUserDataAuthesByRefCode(userTicket.getId(), jsonObject.getString("refCode")));
	}

	/**
	 * 获取数据权限详情列表
	 * @param json key: accessToken, refreshToken, refCode
	 * @return
	 */
	@PostMapping(value = "/listDataAuthDetails.api")
	@ResponseBody
	public BaseOutput<Map<String, Map>> listDataAuthDetails(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		UserTicket userTicket = authService.getUserTicket(jsonObject.getString("UAP_accessToken"), jsonObject.getString("UAP_refreshToken"));
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		String refCode = jsonObject.getString("refCode");
		if(refCode == null){
			return BaseOutput.failure("refCode不存在").setCode(ResultCode.PARAMS_ERROR);
		}
		// 查询数据权限引用dataAuthRef
		DataAuthRef dataAuthRef = DTOUtils.newInstance(DataAuthRef.class);
		dataAuthRef.setCode(refCode);
		List<DataAuthRef> dataAuthRefs = this.dataAuthRefService.list(dataAuthRef);
		if (CollectionUtils.isEmpty(dataAuthRefs)) {
			return BaseOutput.failure("数据权限引用不存在");
		}
		// 从Redis获取用户有权限的UserDataAuth列表
		List<Map> userDataAuthes = this.dataAuthRedis.dataAuth(refCode, userTicket.getId(), userTicket.getSystemType());
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
	 * @param json key: accessToken, refreshToken, oldPassword, newPassword, confirmPassword
	 * @return
	 */
	@PostMapping(value = "/changePwd.api")
	@ResponseBody
	public BaseOutput changePwd(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String accessToken = jsonObject.getString("accessToken");
		String refreshToken = jsonObject.getString("refreshToken");
		String oldPassword = jsonObject.getString("oldPassword");
		String newPassword = jsonObject.getString("newPassword");
		String confirmPassword = jsonObject.getString("confirmPassword");
		UserTicket userTicket = authService.getUserTicket(accessToken, refreshToken);
		if (userTicket == null) {
			return BaseOutput.failure("用户未登录").setCode(ResultCode.NOT_AUTH_ERROR);
		}
		UserDto userDto = DTOUtils.newInstance(UserDto.class);
		userDto.setId(userTicket.getId());
		userDto.setNewPassword(newPassword);
		userDto.setConfirmPassword(confirmPassword);
		userDto.setOldPassword(oldPassword);
		return userService.changePwd(userTicket.getId(), userDto);
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
	public static void main(String[] args) throws Exception {
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
		String content = "{userName:\"admin\", password:\"123456\", pushId:\"test190212390\", deviceType:\"android\"}";

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