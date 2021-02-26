package com.dili.uap.as.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.as.domain.dto.LoginDto;
import com.dili.uap.as.domain.dto.LoginResult;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * Created by asiam on 2018/5/18 0018.
 */
public interface LoginService {

	/**
	 * 登录验证
	 * 
	 * @param loginDto
	 * @return
	 */
	BaseOutput<String> validate(LoginDto loginDto);

	/**
	 * 根据用户名和密码登录，返回登录结果DTO
	 * 
	 * @param loginDto
	 * @return
	 */
	BaseOutput<LoginResult> loginWeb(LoginDto loginDto);

	/**
	 * 登录并标记(标记到Cookie) 根据用户名和密码登录，返回是否登录成功
	 * 
	 * @param loginDto
	 * @return
	 */
	BaseOutput<Boolean> loginAndTag(LoginDto loginDto);

	/**
	 * 记录登出日志
	 * 
	 * @param user
	 */
	void logLogout(UserTicket user);

	/**
	 * 根据accessToken和refreshToken登录
	 * @param accessToken
	 * @param refreshToken
	 * @return
	 */
	BaseOutput<Boolean> loginByTokens(String accessToken, String refreshToken);

	/**
	 * 锁定用户
	 * 
	 * @param user
	 * @return 是否被锁定
	 */
	boolean lockUser(User user);

	/**
	 * 移动端登录
	 * 
	 * @param loginDto
	 * @return
	 */
	BaseOutput<LoginResult> loginApp(LoginDto loginDto);
}
