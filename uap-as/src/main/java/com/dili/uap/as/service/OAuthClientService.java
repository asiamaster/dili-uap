package com.dili.uap.as.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.as.domain.OAuthClient;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-03-08 12:45:30.
 */
public interface OAuthClientService extends BaseService<OAuthClient, Long> {

    /**
     * 根据code查询客户
     * @param code
     * @return
     */
    OAuthClient getByCode(String code);
}