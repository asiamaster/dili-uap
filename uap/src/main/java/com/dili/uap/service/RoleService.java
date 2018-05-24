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
     * @return
     */
    List<SystemResourceDto> list();
}