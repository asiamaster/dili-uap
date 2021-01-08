package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.ResourceLink;

import java.util.Map;

public interface ResourceLinkMapper extends MyMapper<ResourceLink> {

    /**
     * 根据资源id删除有资源链接权限的角色菜单权限
     * @param resourceId
     * @return
     */
    int deleteRoleMenuByResourceId(Long resourceId);

    /**
     * 根据有资源权限的角色， 批量新增角色菜单关系
     * @param param menuId和resourceId
     * @return
     */
    int batchInsertRoleLink(Map param);
}