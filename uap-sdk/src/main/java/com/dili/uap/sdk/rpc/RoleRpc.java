package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Role;
import com.dili.uap.sdk.domain.dto.RoleUserDto;
import com.dili.uap.sdk.domain.dto.UserRoleIdDto;

import java.util.Collection;
import java.util.List;

@Restful("${uap.contextPath}")
public interface RoleRpc {

	@POST("/roleApi/listByUser.api")
	BaseOutput<List<Role>> listByUser(@ReqParam(value = "userId", required = false) Long userId, @ReqParam(value = "userName", required = false) String userName);

	@POST("/roleApi/listRoleByIds.api")
	BaseOutput<List<Role>> listRoleByIds(@VOBody List<String> ids);

	@POST("/userApi/listUserRoleIdByUserIds.api")
	BaseOutput<List<UserRoleIdDto>> listUserRoleIdByUserIds(@VOBody Collection<Long> userIds);

	@POST("/roleApi/listRoleUserByRoleIds.api")
	BaseOutput<List<RoleUserDto>> listRoleUserByRoleIds(@VOBody Collection<Long> roleIds);

}
