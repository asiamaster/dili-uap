package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.domain.dto.UserRoleDto;

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
     * @param userId 用户ID
     * @param user 用户信息
     * @return 结果
     */
    BaseOutput<Object> changePwd(Long userId, UserDto user);

    /**
     * 保存用户信息
     * @param user 用户信息对象
     * @return
     */
    BaseOutput save(User user);

    /**
     * 根据用户ID重置密码
     * @param userId 用户ID
     * @return
     */
    BaseOutput resetPass(Long userId);

    /**
     * 根据用户ID，操作启禁用 用户
     * @param userId 用户ID
     * @param enable 是否启用(true-启用，false-禁用)
     * @return
     */
    BaseOutput upateEnable(Long userId,Boolean enable);

    /**
     * 根据用户ID获取用户所拥有的角色权限
     * @param userId 用户ID
     * @return
     */
    List<UserRoleDto> getUserRolesForTree(Long userId);

    /**
     * 保存用户的角色信息
     * @param userId  用户ID
     * @param roleIds 角色ID
     * @return
     */
    BaseOutput saveUserRoles(Long userId,String[] roleIds);

}