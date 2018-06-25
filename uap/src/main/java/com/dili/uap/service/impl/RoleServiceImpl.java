package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.*;
import com.dili.uap.domain.*;
import com.dili.uap.sdk.domain.System;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.glossary.MenuType;
import com.dili.uap.glossary.Yn;
import com.dili.uap.service.RoleService;
import com.google.common.collect.Lists;
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
    SystemMapper systemMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    RoleResourceMapper roleResourceMapper;
    @Autowired
    UserRoleMapper userRoleMapper;


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
                updateExactSimple(role);
            }else{
                insertSelective(role);
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
                    this.updateExactSimple(role);
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
        List<System> systemList = systemMapper.selectAll();
        if (CollectionUtils.isEmpty(systemList)){
            return null;
        }
        //加载所有的系统菜单-资源
        List<SystemResourceDto> target = getActualDao().getRoleMenuAndResource();
        //根据角色ID加载对应的菜单-资源信息
        List<SystemResourceDto> checkedRoleList = getActualDao().getRoleMenuAndResourceByRoleId(roleId);
        //选中的角色信息
        Set<String> checkedRole = Sets.newHashSet();
        //选中的系统
        Set<String> checkedSystem = Sets.newHashSet();
        //有菜单资源的系统集
        Set<Long> hasMenu = Sets.newHashSet();
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
                //设置节点为关闭
//                s.setState("closed");
                s.setTreeId(UapConstants.MENU_PREFIX + s.getTreeId());
            } else {
                s.setTreeId(UapConstants.RESOURCE_PREFIX + s.getTreeId());
                //设置节点为开启
//                s.setState("open");
            }
            //只有在菜单中，才会存在父ID为空的情况
            if (StringUtils.isBlank(s.getParentId())) {
                //如果父ID为空，则设置父ID为系统ID
                s.setParentId(UapConstants.SYSTEM_PREFIX + s.getSystemId());
            } else {
                //如果父ID不为空，因为资源本身不存在父ID，所以统一更改父ID为菜单的前缀
                s.setParentId(UapConstants.MENU_PREFIX + s.getParentId());
            }
            //如果角色-资源信息已存在关联
            if (checkedRole.contains(s.getTreeId())) {
                s.setChecked(true);
                //用 ParentId ，以防因为当前的数据是资源，会出现空指针问题
                checkedSystem.add(s.getParentId());
            } else {
                s.setChecked(false);
            }
            if (MenuType.DIRECTORY.getCode().intValue() == Integer.parseInt(s.getType())) {
                s.setState("closed");
            }
            s.setType(MenuType.getMenuType(Integer.parseInt(s.getType())).getName());
            hasMenu.add(s.getSystemId());
        });

        /**
         * 遍历系统信息，存入到需要显示的树集中
         */
        systemList.stream().forEach(s -> {
            if (hasMenu.contains(s.getId())){
                SystemResourceDto dto = DTOUtils.newDTO(SystemResourceDto.class);
                dto.setTreeId(UapConstants.SYSTEM_PREFIX + s.getId());
                dto.setName(s.getName());
                dto.setDescription(s.getDescription());
                dto.setState("closed");
                dto.setType("系统");
                if (checkedSystem.contains(dto.getTreeId())){
                    dto.setChecked(true);
                }
                target.add(dto);
            }
        });

        return target;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveRoleMenuAndResource(Long roleId, String[] resourceIds) {
        //勾选的菜单数据集合
        List<RoleMenu> roleMenus = Lists.newArrayList();
        //勾选的资源ID集合
        List<RoleResource> roleResources = Lists.newArrayList();
        /**
         * 循环所勾选的资源信息，分割出菜单、资源信息
         */
        if (null != resourceIds && resourceIds.length > 0) {
            for (String id : resourceIds) {
                //如果是菜单关系，则组装角色-菜单信息
                if (id.startsWith(UapConstants.MENU_PREFIX)) {
                    RoleMenu roleMenu = DTOUtils.newDTO(RoleMenu.class);
                    roleMenu.setMenuId(Long.valueOf(id.replace(UapConstants.MENU_PREFIX, "")));
                    roleMenu.setRoleId(roleId);
                    roleMenus.add(roleMenu);
                }
                //如果是资源关系，则组装角色-资源信息
                if (id.startsWith(UapConstants.RESOURCE_PREFIX)) {
                    RoleResource roleResource = DTOUtils.newDTO(RoleResource.class);
                    roleResource.setResourceId(Long.valueOf(id.replace(UapConstants.RESOURCE_PREFIX, "")));
                    roleResource.setRoleId(roleId);
                    roleResources.add(roleResource);
                }
            }
        }
        //删除对应的角色-菜单信息
        getActualDao().deleteRoleMenuByRoleId(roleId);
        //删除对应的角色-资源信息
        getActualDao().deleteRoleResourceByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleMenus)) {
            roleMenuMapper.insertList(roleMenus);
        }
        if (CollectionUtils.isNotEmpty(roleResources)) {
            roleResourceMapper.insertList(roleResources);
        }
        return BaseOutput.success("操作成功");
    }

    @Override
    public BaseOutput unbindRoleUser(Long roleId, Long userId) {
        if (null == roleId || null == userId) {
            return BaseOutput.failure("参数错误");
        }
        UserRole ur = DTOUtils.newDTO(UserRole.class);
        ur.setRoleId(roleId);
        ur.setUserId(userId);
        List<UserRole> userRoles = userRoleMapper.select(ur);
        userRoles.stream().forEach(userRole -> userRoleMapper.deleteByPrimaryKey(userRole.getId()));
        return BaseOutput.success("解绑成功");
    }

    @Override
    public List<Role> listByUserId(Long userId) {
        return getActualDao().listByUserId(userId);
    }
}