package com.dili.uap.service.impl;

import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.dao.ResourceLinkMapper;
import com.dili.uap.domain.ResourceLink;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.ResourceLinkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-04-15 15:55:32.
 */
@Service
public class ResourceLinkServiceImpl extends BaseServiceImpl<ResourceLink, Long> implements ResourceLinkService {

    public ResourceLinkMapper getActualDao() {
        return (ResourceLinkMapper)getDao();
    }

    @Transactional
    @Override
    public void addResourceLink(ResourceLink resourceLink){
        List<ResourceLink> resourceLinks = list(resourceLink);
        if (!resourceLinks.isEmpty()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "已经绑定资源链接");
        }
        insertSelective(resourceLink);
        deleteRoleMenuByResourceId(resourceLink.getResourceId());
        batchInsertRoleLink(resourceLink.getMenuId(), resourceLink.getResourceId());
        //记录日志
        LoggerContext.put("resourceId", resourceLink.getResourceId());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, resourceLink.getId());
        LoggerContext.put("menuId", resourceLink.getMenuId());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket != null) {
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
            LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        }

    }

    @Override
    public int deleteRoleMenuByResourceId(Long resourceId) {
        return getActualDao().deleteRoleMenuByResourceId(resourceId);
    }

    @Override
    public int batchInsertRoleLink(Long menuId, Long resourceId) {
        Map<String, Long> param = new HashMap<>(4);
        param.put("menuId", menuId);
        param.put("resourceId", resourceId);
        return getActualDao().batchInsertRoleLink(param);
    }

    @Override
    @Transactional
    public void deleteResourceLink(Long id){
        ResourceLink resourceLink = this.get(id);
        //先删除角色菜单关系
        deleteRoleMenuByResourceId(resourceLink.getResourceId());
        //再删除链接
        this.delete(id);
        LoggerContext.put("resourceId", resourceLink.getResourceId());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, resourceLink.getId());
        LoggerContext.put("menuId", resourceLink.getMenuId());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket != null) {
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
            LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        }
    }
}