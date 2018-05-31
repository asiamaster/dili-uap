package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.dto.SystemResourceDto;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 11:45:41.
 */
public interface RoleService extends BaseService<Role, Long> {

    /**
     * 根据ID删除角色信息
     * @param id  角色ID
     * @return  结果信息
     */
    BaseOutput<String> del(Long id);

    /**
     * 保存角色信息
     * @param role 角色信息对象
     * @return 操作结果
     */
    BaseOutput save(Role role);

    /**
     * 获取系统及相关的菜单、资源
     * 并根据当前角色ID进行是否选中的设置
     * @param roleId 角色ID
     * @return
     */
    List<SystemResourceDto> getRoleMenuAndResource(Long roleId);

    /**
     * 保存角色-资源信息
     * @param roleId 角色ID
     * @param resourceIds 资源ID集
     * @return
     */
    BaseOutput saveRoleMenuAndResource(Long roleId,String resourceIds[]);

    /**
     * 解绑角色下的用户
     * @param roleId 角色ID
     * @param userId 用户ID
     * @return
     */
    BaseOutput unbindRoleUser(Long roleId,Long userId);

    /**
     * 获取用户拥有的所有角色信息
     * @param userId 用户ID
     * @return  角色信息
     */
    List<Role> listByUserId(Long userId);
}