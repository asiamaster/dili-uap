package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.ResourceLink;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-04-15 15:55:32.
 */
public interface ResourceLinkService extends BaseService<ResourceLink, Long> {

    /**
     * 添加资源链接
     * 批量删除所有有资源权限的角色菜单权限，并重新添加
     * @param resourceLink
     */
    void addResourceLink(ResourceLink resourceLink);
    /**
     * 根据资源id删除有资源链接权限的角色菜单权限
     * @param resourceId
     * @return
     */
    int deleteRoleMenuByResourceId(Long resourceId);

    /**
     * 根据有资源权限的角色， 批量新增角色菜单关系
     * @param menuId
     * @param resourceId
     * @return
     */
    int batchInsertRoleMenuByResourceId(Long menuId, Long resourceId);

    /**
     * 根据有资源权限的指定角色， 批量新增角色菜单关系
     * @param roleId
     * @param resourceIds
     * @return
     */
    int batchInsertRoleMenuByRoleIdAndResourceIds(Long roleId, List<Long> resourceIds);
    /**
     * 删除资源链接，同时删除角色菜单关系
     * @param id
     */
    void deleteResourceLink(Long id);
}