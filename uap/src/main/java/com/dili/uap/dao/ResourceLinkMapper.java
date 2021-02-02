package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.ResourceLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceLinkMapper extends MyMapper<ResourceLink> {

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
    int batchInsertRoleMenuByResourceId(@Param("menuId") Long menuId, @Param("resourceId") Long resourceId);

    /**
     * 根据角色id和资源id列表, 批量插入角色(内链)菜单
     * 用于角色管理 -> 权限分配，非管理员角色内链权限重新分配
     * @param roleId
     * @param resourceIds
     * @return
     */
    int batchInsertRoleMenuByRoleIdAndResourceIds(@Param("roleId")Long roleId, @Param("resourceIds") List<Long> resourceIds);
}