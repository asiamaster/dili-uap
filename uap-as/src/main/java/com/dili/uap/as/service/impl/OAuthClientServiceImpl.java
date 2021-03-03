package com.dili.uap.as.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.as.domain.OAuthClient;
import com.dili.uap.as.mapper.OAuthClientMapper;
import com.dili.uap.as.service.OAuthClientService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-02-26 14:13:22.
 */
@Service
public class OAuthClientServiceImpl extends BaseServiceImpl<OAuthClient, Long> implements OAuthClientService {

    public OAuthClientMapper getActualDao() {
        return (OAuthClientMapper)getDao();
    }

    @Override
    public OAuthClient getByCode(String code){
        OAuthClient OAuthClient = DTOUtils.newInstance(OAuthClient.class);
        OAuthClient.setCode(code);
        return getActualDao().selectOne(OAuthClient);
    }
}