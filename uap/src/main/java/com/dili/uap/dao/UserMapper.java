package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    /**
     * 根据角色ID查询用户列表信息
     * @param roleId  角色ID
     * @return  用户列表
     */
    List<User> findUserByRole(Long roleId);

    /**
     * 根据条件联合查询数据信息
     * @param user
     * @return
     */
    List<UserDto> selectForPage(User user);
}