package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.dto.MenuDto;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.service.MenuService;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.SystemConfigService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    SystemConfigService systemConfigService;
    @Autowired
    MenuMapper menuMapper;

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
    public List<SystemResourceDto> list() {
        List<MenuDto> menuDtos = menuMapper.selectMenuDto();
        List<SystemResourceDto> target = new ArrayList(menuDtos.size());
        for (MenuDto menuDto : menuDtos) {
            SystemResourceDto dto = new SystemResourceDto();
            dto.setId("menu_"+menuDto.getId());
            dto.setName(menuDto.getName());
            if (null == menuDto.getParentId()){
                dto.setParentId("sys_"+menuDto.getSystemId());
            }else{
                dto.setParentId("menu_"+menuDto.getParentId());
            }
            target.add(dto);
            if (CollectionUtils.isNotEmpty(menuDto.getResources())) {
                for (Resource resource : menuDto.getResources()) {
                    SystemResourceDto resourceDto = new SystemResourceDto();
                    resourceDto.setId("resource_"+resource.getId());
                    resourceDto.setName(resource.getName());
                    resourceDto.setParentId(dto.getId());
                    target.add(resourceDto);
                }
            }
        }
        return target;
    }
}