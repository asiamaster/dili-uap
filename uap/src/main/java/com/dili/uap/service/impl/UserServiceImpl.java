package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.RoleMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.User;
import com.dili.uap.domain.UserRole;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.domain.dto.UserRoleDto;
import com.dili.uap.glossary.UserState;
import com.dili.uap.manager.UserManager;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.MD5Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-18 10:46:46.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

	@Autowired
	private MD5Util md5Util;

	@Autowired
	private UserManager userManager;

	public UserMapper getActualDao() {
		return (UserMapper) getDao();
	}
	@Autowired
	RoleMapper roleMapper;
	@Autowired
	UserRoleMapper userRoleMapper;
	@Autowired
	FirmMapper firmMapper;

	@Override
	public void logout(String sessionId) {
		this.userManager.clearSession(sessionId);
	}

	@Override
	public List<User> findUserByRole(Long roleId) {
		return getActualDao().findUserByRole(roleId);
	}

	@Transactional
	@Override
	public BaseOutput<Object> changePwd(Long userId, UserDto user) {
		if (userId == null) {
			return BaseOutput.failure("您还未登录");
		}
		if (StringUtils.isBlank(user.getOldPassword())) {
			return BaseOutput.failure("当前密码不能为空");
		}
		if (StringUtils.isBlank(user.getNewPassword())) {
			return BaseOutput.failure("新密码不能为空");
		}
		if (user.getNewPassword().trim().length()<6) {
			return BaseOutput.failure("新密码长度限定为6-20");
		}
		if (user.getNewPassword().trim().length()>20) {
			return BaseOutput.failure("新密码长度限定为6-20");
		}
		
		if (user.getNewPassword().equals(user.getOldPassword())) {
			return BaseOutput.failure("当前密码与新密码不能相同");
		}
		if (!user.getNewPassword().equals(user.getConfirmPassword())) {
			return BaseOutput.failure("两次密码输入不一致,请重新输入");
		}

		User userInDB = this.get(userId);
		if (userInDB == null) {
			return BaseOutput.failure("修改密码失败");
		}
		// 进行原密码比较
		if (!StringUtils.equalsIgnoreCase(userInDB.getPassword(), this.encryptPwd(user.getOldPassword()))) {
			return BaseOutput.failure("当前密码有误,请重新输入");
		}

		userInDB.setModified(new Date());
		// 加密并更新密码

		userInDB.setPassword(this.encryptPwd(user.getNewPassword()));
		this.updateExactSimple(userInDB);

		return BaseOutput.success("修改密码成功");
	}

	private String encryptPwd(String passwd) {
		return md5Util.getMD5ofStr(passwd).substring(6, 24);
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public BaseOutput save(User user) {
		//验证邮箱是否重复
		User query = DTOUtils.newDTO(User.class);
		query.setEmail(user.getEmail());
		List<User> userList = getActualDao().select(query);
        //用户新增
        if (null == user.getId()) {
			if (CollectionUtils.isNotEmpty(userList)) {
				return BaseOutput.failure("邮箱已存在");
			}
        	query.setEmail(null);
            query.setUserName(user.getUserName());
            userList = getActualDao().select(query);
            if (CollectionUtils.isNotEmpty(userList)) {
                return BaseOutput.failure("用户账号已存在");
            } else {
                user.setState(UserState.INACTIVE.getCode());
            }
            user.setPassword(encryptPwd(user.getPassword()));
            this.insertExactSimple(user);
        } else {
			User update = this.get(user.getId());
			if (CollectionUtils.isNotEmpty(userList)) {
				//匹配是否有用户ID不等当前修改记录的用户
				boolean result = userList.stream().anyMatch(u -> !u.getId().equals(user.getId()));
				if (result) {
					return BaseOutput.failure("邮箱已存在");
				}
			}
			update.setRealName(user.getRealName());
			update.setCellphone(user.getCellphone());
			update.setEmail(user.getEmail());
			update.setPosition(user.getPosition());
			update.setCardNumber(user.getCardNumber());
			update.setDepartmentId(user.getDepartmentId());
			update.setDescription(user.getDescription());
            this.updateExactSimple(update);
        }
        return BaseOutput.success("操作成功");
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseOutput resetPass(Long userId) {
		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		user.setPassword(encryptPwd(UapConstants.DEFAULT_PASS));
		user.setState(UserState.INACTIVE.getCode());
		this.updateSelective(user);
		return BaseOutput.success("重置成功");
	}

	@Override
	public BaseOutput upateEnable(Long userId, Boolean enable) {
		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		if (enable) {
			user.setState(UserState.NORMAL.getCode());
		} else {
			user.setState(UserState.DISABLED.getCode());
		}
		this.updateSelective(user);
		return BaseOutput.success("操作成功");
	}

	@Override
	public List<UserRoleDto> getUserRolesForTree(Long userId) {
		//获取需要分配角色的用户信息
		User user = this.get(userId);
		//预分配角色的用户，是否属于集团用户
		Boolean isGroup = false;
		if (UapConstants.GROUP_CODE.equalsIgnoreCase(user.getFirmCode())) {
			isGroup = true;
		}
		List<Firm> firmList = null;
		List<Role> roleList = null;
		if (isGroup) {
			roleList = roleMapper.selectAll();
			firmList = firmMapper.selectAll();
		} else {
			//根据用户查询该用户对应的市场的角色信息
			Role roleQuery = DTOUtils.newDTO(Role.class);
			roleQuery.setFirmCode(user.getFirmCode());
			roleList = roleMapper.select(roleQuery);
			//查询对应的市场信息
			Firm firmQuery = DTOUtils.newDTO(Firm.class);
			firmQuery.setCode(user.getFirmCode());
			firmList = firmMapper.select(firmQuery);
		}
		//用户对应的市场，存在角色信息，则需进一步检索用户已有的角色
		if (CollectionUtils.isNotEmpty(roleList)) {
			UserRole userRoleQuery = DTOUtils.newDTO(UserRole.class);
			userRoleQuery.setUserId(userId);
			Set<Long> userRoleIds = userRoleMapper.getRoleIdsByUserId(user.getId());
			List<UserRoleDto> userRoleDtos = Lists.newArrayList();
			/**
			 * 遍历角色信息，设置是否选中
			 * 末级节点，设置为open状态
			 */
			roleList.stream().forEach(role -> {
				UserRoleDto dto = DTOUtils.newDTO(UserRoleDto.class);
				dto.setName(role.getRoleName());
				dto.setParentId(role.getFirmCode());
				dto.setTreeId(String.valueOf(role.getId()));
				if (userRoleIds.contains(role.getId())){
					dto.setChecked(true);
				}else{
					dto.setChecked(false);
				}
				userRoleDtos.add(dto);
			});
			firmList.stream().forEach(firm -> {
				UserRoleDto dto = DTOUtils.newDTO(UserRoleDto.class);
				dto.setName(firm.getName());
				dto.setTreeId(firm.getCode());
				dto.setParentId("");
				dto.setChecked(false);
				userRoleDtos.add(dto);
			});
			return userRoleDtos;
		}
		return null;
	}

	@Override
	public EasyuiPageOutput listEasyuiPage(User domain, boolean useProvider) throws Exception {
		if (domain.getRows() != null && domain.getRows() >= 1) {
			PageHelper.startPage(domain.getPage(), domain.getRows());
		}
		List<UserDto> users = getActualDao().selectForPage(domain);
		long total = users instanceof Page ? ((Page)users).getTotal() : (long)users.size();
		List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, users) : users;
		return new EasyuiPageOutput((int)total, results);
	}
}