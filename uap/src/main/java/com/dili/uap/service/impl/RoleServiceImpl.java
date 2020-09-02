package com.dili.uap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.dao.RoleMenuMapper;
import com.dili.uap.dao.RoleResourceMapper;
import com.dili.uap.dao.SystemMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.RoleMenu;
import com.dili.uap.domain.RoleResource;
import com.dili.uap.domain.UserRole;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.glossary.MenuType;
import com.dili.uap.glossary.Yn;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.Role;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.RoleUserDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.RoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-18 11:45:41.
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

	public RoleMapper getActualDao() {
		return (RoleMapper) getDao();
	}

	@Value("${uap.adminName:admin}")
	private String adminName;
	@Autowired
	SystemMapper systemMapper;
	@Autowired
	RoleMenuMapper roleMenuMapper;
	@Autowired
	RoleResourceMapper roleResourceMapper;
	@Autowired
	UserRoleMapper userRoleMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseOutput<String> del(Long id) {
		/**
		 * 验证该角色下是否有用户信息，如果存在，则不能删除
		 */
		Long count = getActualDao().countUserByRoleId(id);
		if (null != count && count >= 1L) {
			return BaseOutput.failure("该角色下有关联用户，不能删除");
		}
		Role role = this.getActualDao().selectByPrimaryKey(id);
		if (!role.getLeaf()) {
			return BaseOutput.failure("包含子角色不能删除");
		}
		delete(id);
		if (role.getParentId() != null) {
			Role query = DTOUtils.newInstance(Role.class);
			query.setParentId(role.getParentId());
			count = (long) this.getActualDao().selectCount(query);
			if (count <= 0) {
				Role parent = this.getActualDao().selectByPrimaryKey(role.getParentId());
				parent.setLeaf(true);
				this.getActualDao().updateByPrimaryKeySelective(parent);
			}
		}
		Map param = new HashMap(1);
		param.put("roleId", id);
		// 删除对应的角色-菜单信息
		getActualDao().deleteRoleMenuByRoleId(param);
		// 删除对应的角色-资源信息
		getActualDao().deleteRoleResourceByRoleId(param);
		// 删除角色信息
		return BaseOutput.success();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseOutput save(Role role) {
		// 检查同一市场内，名称是否重复
		Role query = DTOUtils.newInstance(Role.class);
		query.setRoleName(role.getRoleName());
		query.setFirmCode(role.getFirmCode());
		List<Role> roles = this.list(query);
		if (CollectionUtils.isEmpty(roles)) {
			// 不存在重名的情况下
			if (null != role.getId()) {
				updateExactSimple(role);
			} else {
				// 更新叶节点状态
				if (role.getParentId() != null) {
					query = DTOUtils.newInstance(Role.class);
					query.setId(role.getParentId());
					Role parent = this.getActualDao().selectOne(query);
					if (parent != null && parent.getLeaf()) {
						role.setMergeName(parent.getMergeName() + "," + role.getRoleName());
						parent.setLeaf(false);
						this.getActualDao().updateByPrimaryKeySelective(parent);
					}
				}
				insertSelective(role);
			}
		} else {
			// 存在重名的情况下
			if (null != role.getId()) {
				if (roles.size() > 1) {
					return BaseOutput.failure("操作失败，角色名称存在重复");
				}
				Role temp = roles.get(0);
				// 如果查询出来的数据，跟当前的一样，则认为是没有修改名称
				if (temp.getId().equals(role.getId())) {
					this.updateExactSimple(role);
				} else {
					return BaseOutput.failure("操作失败，角色名称存在重复");
				}
			} else {
				// 新增，则提示名称有重复
				return BaseOutput.failure("操作失败，角色名称存在重复");
			}
		}
		return BaseOutput.success("操作成功");
	}

	@Override
	public List<SystemResourceDto> getRoleMenuAndResource(Long roleId) {
		// 检索所以的系统信息
		List<Systems> systemList = systemMapper.selectAll();
		if (CollectionUtils.isEmpty(systemList)) {
			return null;
		}
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return new ArrayList<>(1);
		}
		// 加载所有的系统菜单-资源
		List<SystemResourceDto> target;
		// admin用户可以查看所有权限
		if (userTicket.getUserName().equalsIgnoreCase(adminName)) {
			target = getActualDao().getRoleMenuAndResource();
		} else {// 非admin用户只能查看各自有权限的菜单和资源
			target = getActualDao().getRoleMenuAndResourceByUserId(userTicket.getId());
		}
		// 根据角色ID加载对应的菜单-资源信息
		List<SystemResourceDto> checkedRoleList = getActualDao().getRoleMenuAndResourceByRoleId(roleId);
		// 选中的角色信息
		Set<String> checkedRole = Sets.newHashSet();
		// 选中的系统
		Set<String> checkedSystem = Sets.newHashSet();
		// 有菜单资源的系统集
		Set<Long> hasMenu = Sets.newHashSet();
		/**
		 * 遍历已选择的资源信息，根据是否是菜单，添加不同的前缀
		 */
		checkedRoleList.stream().forEach(s -> {
			// 如果是菜单，则ID加上对应前缀
			if (s.getMenu().intValue() == Yn.YES.getCode().intValue()) {
				checkedRole.add(UapConstants.MENU_PREFIX + s.getTreeId());
			} else {
				checkedRole.add(UapConstants.RESOURCE_PREFIX + s.getTreeId());
			}
		});
		/**
		 * 遍历菜单-资源信息，根据是否菜单，设置tree中菜单ID显示信息 设置是否选中等相关信息
		 */
		target.stream().forEach(s -> {
			// 如果是菜单，则ID加上对应前缀
			if (s.getMenu().intValue() == Yn.YES.getCode().intValue()) {
				// 设置节点为关闭
//                s.setState("closed");
				s.setTreeId(UapConstants.MENU_PREFIX + s.getTreeId());
			} else {
				s.setTreeId(UapConstants.RESOURCE_PREFIX + s.getTreeId());
				// 设置节点为开启
//                s.setState("open");
			}
			// 只有在菜单中，才会存在父ID为空的情况
			if (StringUtils.isBlank(s.getParentId())) {
				// 如果父ID为空，则设置父ID为系统ID
				s.setParentId(UapConstants.SYSTEM_PREFIX + s.getSystemId());
			} else {
				// 如果父ID不为空，因为资源本身不存在父ID，所以统一更改父ID为菜单的前缀
				s.setParentId(UapConstants.MENU_PREFIX + s.getParentId());
			}
			// 如果角色-资源信息已存在关联
			if (checkedRole.contains(s.getTreeId())) {
				s.setChecked(true);
				// 用 ParentId ，以防因为当前的数据是资源，会出现空指针问题
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
			if (hasMenu.contains(s.getId())) {
				SystemResourceDto dto = DTOUtils.newInstance(SystemResourceDto.class);
				dto.setTreeId(UapConstants.SYSTEM_PREFIX + s.getId());
				dto.setName(s.getName());
				dto.setDescription(s.getDescription());
				dto.setState("closed");
				dto.setType("系统");
				if (checkedSystem.contains(dto.getTreeId())) {
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
		// 勾选的菜单数据集合
		List<RoleMenu> roleMenus = Lists.newArrayList();
		List<RoleMenu> toInsertRoleMenus = null;
		// 勾选的资源ID集合
		List<RoleResource> roleResources = Lists.newArrayList();
		List<RoleResource> toInsertRoleResources = null;
		/**
		 * 循环所勾选的资源信息，分割出菜单、资源信息
		 */
		if (null != resourceIds && resourceIds.length > 0) {
			for (String id : resourceIds) {
				// 如果是菜单关系，则组装角色-菜单信息
				if (id.startsWith(UapConstants.MENU_PREFIX)) {
					RoleMenu roleMenu = DTOUtils.newInstance(RoleMenu.class);
					roleMenu.setMenuId(Long.valueOf(id.replace(UapConstants.MENU_PREFIX, "")));
					roleMenu.setRoleId(roleId);
					roleMenus.add(roleMenu);
				}
				// 如果是资源关系，则组装角色-资源信息
				if (id.startsWith(UapConstants.RESOURCE_PREFIX)) {
					RoleResource roleResource = DTOUtils.newInstance(RoleResource.class);
					roleResource.setResourceId(Long.valueOf(id.replace(UapConstants.RESOURCE_PREFIX, "")));
					roleResource.setRoleId(roleId);
					roleResources.add(roleResource);
				}
			}
		}
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		// 超级管理员权限最大
		if (userTicket.getUserName().equals(this.adminName)) {
			RoleMenu rmCondition = DTOUtils.newInstance(RoleMenu.class);
			rmCondition.setRoleId(roleId);
			this.roleMenuMapper.delete(rmCondition);
			RoleResource rrCondition = DTOUtils.newInstance(RoleResource.class);
			rrCondition.setRoleId(roleId);
			this.roleResourceMapper.delete(rrCondition);
			if (CollectionUtils.isNotEmpty(roleMenus)) {
				this.roleMenuMapper.insertList(roleMenus);
			}
			if (CollectionUtils.isNotEmpty(roleResources)) {
				this.roleResourceMapper.insertList(roleResources);
			}
			List<Long> menuIds = new ArrayList<>(roleMenus.size());
			roleMenus.forEach(rm -> menuIds.add(rm.getMenuId()));
			LoggerContext.put("roleMenus", JSON.toJSONString(menuIds));
			List<Long> logResourceIds = new ArrayList<>(roleResources.size());
			roleResources.forEach(rr -> logResourceIds.add(rr.getResourceId()));
			LoggerContext.put("roleResources", JSON.toJSONString(logResourceIds));
			return BaseOutput.success("操作成功");
		}
		final StringBuilder sb = new StringBuilder();
		if (CollectionUtils.isNotEmpty(roleMenus)) {
			toInsertRoleMenus = this.getActualDao().selectInsertRoleMenuByLoggedUserId(roleMenus, roleId, userTicket.getId());
		}
		if (CollectionUtils.isNotEmpty(roleResources)) {
			toInsertRoleResources = this.getActualDao().selectInsertRoleResourceByLoggedUserId(roleResources, roleId, userTicket.getId());
		}
		Map param = new HashMap<>(2);
		param.put("roleId", roleId);
		param.put("loginUserId", userTicket.getId());
		// 删除对应的角色-菜单信息
		getActualDao().deleteRoleMenuByRoleId(param);
		// 删除对应的角色-资源信息
		getActualDao().deleteRoleResourceByRoleId(param);
		if (CollectionUtils.isNotEmpty(toInsertRoleMenus)) {
			roleMenuMapper.insertList(toInsertRoleMenus);
		}
		if (CollectionUtils.isNotEmpty(toInsertRoleResources)) {
			roleResourceMapper.insertList(toInsertRoleResources);
		}
		List<SystemResourceDto> resources = this.getActualDao().selectLimittedUpdateMenuList(roleMenus, roleResources, roleId, userTicket.getId());
		if (CollectionUtils.isNotEmpty(resources)) {
			sb.append("当前登录用户权限不足，以下权限修改不成功：");
			resources.forEach(s -> sb.append(s.getName()).append(','));
		}
		List<Long> menuIds = new ArrayList<>(toInsertRoleMenus.size());
		toInsertRoleMenus.forEach(rm -> menuIds.add(rm.getMenuId()));
		LoggerContext.put("roleMenus", JSON.toJSONString(toInsertRoleMenus));
		List<Long> logResourceIds = new ArrayList<>(toInsertRoleResources.size());
		toInsertRoleResources.forEach(rr -> logResourceIds.add(rr.getResourceId()));
		LoggerContext.put("roleResources", JSON.toJSONString(toInsertRoleResources));
		return BaseOutput.success(StringUtils.isEmpty(sb) ? "操作成功" : sb.substring(0, sb.length() - 1));
	}

	@Override
	public BaseOutput unbindRoleUser(Long roleId, Long userId) {
		if (null == roleId || null == userId) {
			return BaseOutput.failure("参数错误");
		}
		UserRole ur = DTOUtils.newInstance(UserRole.class);
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

	@Override
	public List<Role> listByUserName(String userName) {
		return getActualDao().listByUserName(userName);
	}

	@Override
	public List<RoleUserDto> listRoleUserByRoleIds(List<Long> roleIds) {
		return this.getActualDao().listRoleUserByRoleIds(roleIds);
	}

	@Override
	public Integer countAll() {
		return this.getActualDao().selectCount(DTOUtils.newInstance(Role.class));
	}

}