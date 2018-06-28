package com.dili.uap.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.domain.dto.LoginDto;

/**
 * Created by asiam on 2018/5/18 0018.
 */
public interface LoginService {

    /**
     * 登录验证
     * @param loginDto
     * @return
     */
    BaseOutput<String> validate(LoginDto loginDto);

    /**
     * 根据用户名和密码登录，返回登录结果DTO
     * @param loginDto
     * @return
     */
    BaseOutput<LoginResult> login(LoginDto loginDto);

    /**
     * 登录并标记(标记到Cookie)
     * 根据用户名和密码登录，返回是否登录成功
     * @param loginDto
     * @return
     */
    BaseOutput<Boolean> loginAndTag(LoginDto loginDto);

    /**
     * 记录登出日志
     * @param loginLog
     */
    void logLogout(LoginLog loginLog);
}
