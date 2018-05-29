package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 10:46:46.
 */
public interface UserService extends BaseService<User, Long> {

    /**
     * 登出
     * @param sessionId
     */
    void logout(String sessionId);

    /**
     * 根据角色ID查询用户列表信息
     * @param roleId  角色ID
     * @return  用户列表
     */
    List<User> findUserByRole(Long roleId);
    
    /**
     * 修改密码
     * @param User  用户信息
     * @return  结果
     */
    BaseOutput<Object>changePwd(Long userId,UserDto user);

}