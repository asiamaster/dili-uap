package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.retrofitful.annotation.*;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserDepartmentRole;
import com.dili.uap.sdk.domain.dto.UserDepartmentRoleQuery;
import com.dili.uap.sdk.domain.dto.UserQuery;

import java.util.List;

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

	@POST("/userApi/findCurrentFirmUsersByResourceCode.api")
	BaseOutput<List<User>> findCurrentFirmUsersByResourceCode(@ReqParam("firmCode") String firmCode, @ReqParam("resourceCode") String resourceCode);

	@POST("/userApi/findUsersByResourceCode.api")
	BaseOutput<List<User>> findUsersByResourceCode(@ReqParam("resourceCode") String resourceCode, @ReqParam("marketId") Long marketId);

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

	/**
	 * 小程序修改密码
	 *
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @return
	 */
	@POST("/userApi/changePwdForApp.api")
	BaseOutput<Object> changePwdForApp(@VOField("userId") Long userId, @VOField("oldPassword") String oldPassword, @VOField("newPassword") String newPassword, @VOField("confirmPassword") String confirmPassword);

	/**
	 * 根据部门id获取部门人数
	 *
	 * @param ids eg:1,2,3
	 * @param date 截止创建日期,yyyy-MM-dd
	 * @return
	 */
	@POST("/userApi/getUserCountByDepartmentIds.api")
	BaseOutput<Object> getUserCountByDepartmentIds(@ReqParam("ids") String ids, @ReqParam("date") String date);
}
