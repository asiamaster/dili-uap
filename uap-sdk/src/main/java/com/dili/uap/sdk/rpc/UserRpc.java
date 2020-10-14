package com.dili.uap.sdk.rpc;

import java.util.List;

import com.dili.ss.retrofitful.annotation.*;
import com.dili.uap.sdk.domain.dto.UserQuery;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserDepartmentRole;
import com.dili.uap.sdk.domain.dto.UserDepartmentRoleQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <B>Description</B> <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/24 17:46
 */
@Restful("${uap.contextPath}")
public interface UserRpc {

	@POST("/userApi/get.api")
	BaseOutput<User> get(@VOBody Long id);

	@POST("/userApi/list.api")
	BaseOutput<List<User>> list(@VOBody User user);

	@POST("/userApi/listUserByIds.api")
	BaseOutput<List<User>> listUserByIds(@VOBody List<String> ids);

	@POST("/userApi/listByExample.api")
	BaseOutput<List<User>> listByExample(@VOBody(required = false) UserQuery userQuery);

	@POST("/userApi/listPageByExample.api")
	PageOutput<List<User>> listPageByExample(@VOBody(required = false) UserQuery userQuery);

	@POST("/userApi/get.api")
	BaseOutput<User> findUserById(@VOBody Long id);

	@POST("/userApi/listUserByRoleId.api")
	BaseOutput<List<User>> listUserByRoleId(@VOBody Long roleId);

	@POST("/userApi/findUserContainDepartmentAndRole.api")
	BaseOutput<List<UserDepartmentRole>> findUserContainDepartmentAndRole(@VOBody(required = false) UserDepartmentRoleQuery dto);

	/**
	 * 查询市场下具有按钮权限的用户
	 * 
	 * @param firmCode     市场编码
	 * @param resourceCode 权限编码
	 * @return
	 */
	@POST("/userApi/findCurrentFirmUsersByResourceCode.api")
	BaseOutput<List<User>> findCurrentFirmUsersByResourceCode(@ReqParam("firmCode") String firmCode, @ReqParam("resourceCode") String resourceCode);

	/**
	 * 查询市场下具有按钮权限的用户
	 * 
	 * @param firmCode     市场编码
	 * @param resourceCode 权限编码
	 * @return
	 */
	@POST("/userApi/findUsersByResourceCode.api")
	BaseOutput<List<User>> findUsersByResourceCode(@ReqParam("resourceCode") String resourceCode);

	/**
	 * 验证用户密码
	 * 
	 * @param userId   用户id
	 * @param password 密码
	 * @return
	 */
	@POST("/userApi/validatePassword.api")
	BaseOutput<Object> validatePassword(@ReqParam("userId") Long userId, @ReqParam("password") String password);

	/**
	 * 添加用户角色关联
	 *
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@POST("/userApi/saveUserRoles.api")
	BaseOutput<Object> saveUserRoles(@VOField("userId") Long userId, @VOField("roleId") String roleId);

	/**
	 * 删除用户角色关联
	 *
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@POST("/userApi/unbindUserRole.api")
	BaseOutput<Object> unbindUserRole(@VOField("userId") Long userId, @VOField("roleId") String roleId);

	/**
	 *通过app注册用户
	 * @param  userName
	 * @param  realName
	 * @param  cellphone
	 * @param  email
	 * @param  position
	 * @param  cardNumber
	 * @param  firmCode
	 * @param  departmentId
	 * @param  description
	 * @return
	 */
	@POST("/userApi/registeryByApp.api")
	BaseOutput<Object> registeryByApp(@VOField("userName") String userName, @VOField("realName") String realName, @VOField("cellphone") String cellphone, @VOField("email") String email, @VOField("position") String position, @VOField("cardNumber") String cardNumber, @VOField("firmCode") String firmCode, @VOField("departmentId") Long departmentId, @VOField("description") String description);
}
