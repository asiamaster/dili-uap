package com.dili.uap.as.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.as.domain.Client;
import com.dili.uap.as.mapper.ClientMapper;
import com.dili.uap.as.service.ClientService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-02-26 14:13:22.
 */
@Service
public class ClientServiceImpl extends BaseServiceImpl<Client, Long> implements ClientService {

    public ClientMapper getActualDao() {
        return (ClientMapper)getDao();
    }

    @Override
    public Client getByCode(String code){
        Client client = DTOUtils.newInstance(Client.class);
        client.setCode(code);
        return getActualDao().selectOne(client);
    }
}