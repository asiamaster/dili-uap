package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.glossary.UserState;
import com.dili.uap.manager.UserManager;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.MD5Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
		if (user.getNewPassword().equals(user.getOldPassword())) {
			return BaseOutput.failure("当前密码与新密码不能相同");
		}
		if (!user.getNewPassword().equals(user.getConfirmPassword())) {
			return BaseOutput.failure("两次密码不一致");
		}

		User userInDB = this.get(userId);
		if (userInDB == null) {
			return BaseOutput.failure("修改密码失败");
		}
		// 进行原密码比较
		if (!StringUtils.equalsIgnoreCase(userInDB.getPassword(), this.encryptPwd(user.getOldPassword()))) {
			return BaseOutput.failure("当前密码错误");
		}

		userInDB.setModified(new Date());
		// 加密并更新密码

		userInDB.setPassword(this.encryptPwd(user.getNewPassword()));
		this.getActualDao().updateByPrimaryKeyExact(userInDB);

		return BaseOutput.success("修改密码成功");
	}

	private String encryptPwd(String passwd) {
		return md5Util.getMD5ofStr(passwd).substring(6, 24);
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public BaseOutput save(User user) {
        //用户新增
        if (null == user.getId()) {
            User query = DTOUtils.newDTO(User.class);
            query.setUserName(user.getUserName());
            List<User> userList = getActualDao().select(query);
            if (CollectionUtils.isNotEmpty(userList)) {
                return BaseOutput.failure("用户账号已存在");
            } else {
                user.setState(UserState.INACTIVE.getCode());
            }
            user.setPassword(encryptPwd(user.getPassword()));
            this.insertExactSimple(user);
        } else {
            this.updateExactSimple(user);
        }
        return BaseOutput.success("操作成功");
    }
}