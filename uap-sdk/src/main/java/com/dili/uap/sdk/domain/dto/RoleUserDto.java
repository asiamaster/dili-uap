package com.dili.uap.sdk.domain.dto;

import java.util.List;

import com.dili.uap.sdk.domain.Role;
import com.dili.uap.sdk.domain.User;

public interface RoleUserDto extends Role {

	List<User> getUsers();

	void setUsers(List<User> users);
}
