package com.dili.uap.service.impl;

import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.UserPushInfoMapper;
import com.dili.uap.sdk.domain.UserPushInfo;
import com.dili.uap.service.UserPushInfoService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-10-21 14:07:04.
 */
@Service
public class UserPushInfoServiceImpl extends BaseServiceImpl<UserPushInfo, Long> implements UserPushInfoService {

    public UserPushInfoMapper getActualDao() {
        return (UserPushInfoMapper)getDao();
    }
}