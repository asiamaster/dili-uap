package com.dili.uap.as.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.as.domain.Client;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-02-26 14:13:22.
 */
public interface ClientService extends BaseService<Client, Long> {

    /**
     * 根据code查询客户
     * @param code
     * @return
     */
    Client getByCode(String code);
}