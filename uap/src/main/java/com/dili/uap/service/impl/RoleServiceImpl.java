package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.System;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.glossary.Yn;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.SystemService;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 11:45:41.
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    public RoleMapper getActualDao() {
        return (RoleMapper)getDao();
    }

    @Autowired
    SystemService systemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput<String> del(Long id) {
        /**
         * 验证该角色下是否有用户信息，如果存在，则不能删除
         */
        Long count = getActualDao().countUserByRoleId(id);
        if (null != count && count >= 1L){
            return BaseOutput.failure("该角色下有关联用户，不能删除");
        }
        //删除对应的角色-菜单信息
        getActualDao().deleteRoleMenuByRoleId(id);
        //删除对应的角色-资源信息
        getActualDao().deleteRoleResourceByRoleId(id);
        //删除角色信息
        delete(id);
        return BaseOutput.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput save(Role role) {
        //检查同一市场内，名称是否重复
        Role query = DTOUtils.newDTO(Role.class);
        query.setRoleName(role.getRoleName());
        query.setFirmCode(role.getFirmCode());
        List<Role> roles = this.list(query);
        if (CollectionUtils.isEmpty(roles)){
            //不存在重名的情况下
            if (null != role.getId()) {
                getActualDao().updateByPrimaryKeySelective(role);
            }else{
                getActualDao().insert(role);
            }
        }else{
            //存在重名的情况下
            if (null != role.getId()) {
                if (roles.size() > 1) {
                    return BaseOutput.failure("操作失败，角色名称存在重复");
                }
                Role temp = roles.get(0);
                //如果查询出来的数据，跟当前的一样，则认为是没有修改名称
                if (temp.getId().equals(role.getId())){
                    getActualDao().updateByPrimaryKeySelective(role);
                }else{
                    return BaseOutput.failure("操作失败，角色名称存在重复");
                }
            }else{
                //新增，则提示名称有重复
                return BaseOutput.failure("操作失败，角色名称存在重复");
            }
        }
        return BaseOutput.success("操作成功");
    }

    @Override
    public List<SystemResourceDto> getRoleMenuAndResource(Long roleId) {
        //检索所以的系统信息
        List<System> systemList = systemService.list(null);
        if (CollectionUtils.isEmpty(systemList)){
            return null;
        }
        //加载所有的系统菜单-资源
        List<SystemResourceDto> target = getActualDao().getRoleMenuAndResource();
        //根据角色ID加载对应的菜单-资源信息
        List<SystemResourceDto> checkedRoleList = getActualDao().getRoleMenuAndResourceByRoleId(roleId);
        Set<String> checkedRole = Sets.newHashSet();
        /**
         * 遍历已选择的资源信息，根据是否是菜单，添加不同的前缀
         */
        checkedRoleList.stream().forEach(s -> {
            //如果是菜单，则ID加上对应前缀
            if (s.getMenu().intValue() == Yn.YES.getCode().intValue()) {
                checkedRole.add(UapConstants.MENU_PREFIX + s.getTreeId());
            } else {
                checkedRole.add(UapConstants.RESOURCE_PREFIX + s.getTreeId());
            }
        });
        /**
         * 遍历菜单-资源信息，根据是否菜单，设置tree中菜单ID显示信息
         * 设置是否选中等相关信息
         */
        target.stream().forEach(s -> {
            //如果是菜单，则ID加上对应前缀
            if (s.getMenu().intValue() == Yn.YES.getCode().intValue()) {
                s.setTreeId(UapConstants.MENU_PREFIX + s.getTreeId());
            } else {
                s.setTreeId(UapConstants.RESOURCE_PREFIX + s.getTreeId());
            }
            //只有在菜单中，才会存在父ID为空的情况
            if (StringUtils.isBlank(s.getParentId())) {
                //如果父ID为空，则设置父ID为系统ID
                s.setParentId(UapConstants.SYSTEM_PREFIX + s.getSystemId());
            } else {
                //如果父ID不为空，因为资源本身不存在父ID，所以统一更改父ID为菜单的前缀
                s.setParentId(UapConstants.MENU_PREFIX + s.getParentId());
            }
            //设置节点为关闭
            s.setState("closed");
            //如果角色-资源信息已存在关联
            if (checkedRole.contains(s.getTreeId())) {
                s.setChecked(true);
            }
        });

        systemList.stream().forEach(s -> {
            SystemResourceDto dto = DTOUtils.newDTO(SystemResourceDto.class);
            dto.setTreeId(UapConstants.SYSTEM_PREFIX + s.getId());
            dto.setName(s.getName());
            dto.setDescription(s.getDescription());
            target.add(dto);
        });

        return target;
    }
}