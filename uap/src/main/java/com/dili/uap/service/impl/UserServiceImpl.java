package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.User;
import com.dili.uap.manager.UserManager;
import com.dili.uap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 10:46:46.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    @Autowired
    private UserManager userManager;
    public UserMapper getActualDao() {
        return (UserMapper)getDao();
    }

    @Override
    public void logout(String sessionId) {
        this.userManager.clearSession(sessionId);
    }

    @Override
    public List<User> findUserByRole(Long roleId) {
        return getActualDao().findUserByRole(roleId);
    }
}