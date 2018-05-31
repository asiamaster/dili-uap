package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.dto.SystemResourceDto;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    /**
     * 根据角色ID查询该角色下是否存在有用户
     * @param roleId 角色ID
     * @return 用户数
     */
    Long countUserByRoleId(Long roleId);

    /**
     * 根据角色ID删除角色-菜单信息
     * @param roleId 角色ID
     * @return 受影响行数
     */
    Integer deleteRoleMenuByRoleId(Long roleId);

    /**
     * 根据角色ID删除角色-资源信息
     * @param roleId 角色ID
     * @return 受影响行数
     */
    Integer deleteRoleResourceByRoleId(Long roleId);

    /**
     * 加载所有的菜单及资源信息
     * @return
     */
    List<SystemResourceDto> getRoleMenuAndResource();

    /**
     * 根据角色ID查询对应的菜单及资源信息
     * @param roleId 角色ID
     * @return
     */
    List<SystemResourceDto> getRoleMenuAndResourceByRoleId(Long roleId);

    /**
     * 根据用户获取用户所属角色信息
     * @param userId 用户ID
     * @return  角色信息
     */
    List<Role> listByUserId(Long userId);

}