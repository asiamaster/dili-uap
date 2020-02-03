package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.service.UserDataAuthService;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-25 14:28:01.
 */
@Service
public class UserDataAuthServiceImpl extends BaseServiceImpl<UserDataAuth, Long> implements UserDataAuthService {

    public UserDataAuthMapper getActualDao() {
        return (UserDataAuthMapper)getDao();
    }
    /**
     * 根据userDataAuth删除数据
     */
    @Override
    public int delete(UserDataAuth userDataAuth) {
        return getActualDao().delete(userDataAuth);
    }
    /**
     * 根据userDataAuth删除数据
     */
	@Override
	public List<String> listUserDataAuthValueByUserId(Long userId,String code) {
		return getActualDao().selectUserDataAuthValue(userId,code);
	}
}