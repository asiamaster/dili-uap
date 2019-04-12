package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.ResourceLinkMapper;
import com.dili.uap.domain.ResourceLink;
import com.dili.uap.service.ResourceLinkService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-04-11 14:56:58.
 */
@Service
public class ResourceLinkServiceImpl extends BaseServiceImpl<ResourceLink, Long> implements ResourceLinkService {

    public ResourceLinkMapper getActualDao() {
        return (ResourceLinkMapper)getDao();
    }
}