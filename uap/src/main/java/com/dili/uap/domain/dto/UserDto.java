package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.User;

import javax.persistence.Column;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;

public interface UserDto extends User {

	@Transient
	default String getOldPassword() {
		return "123456";
	}

	void setOldPassword(String oldPassword);

	@Transient
	String getNewPassword();

	void setNewPassword(String newPassword);

	@Transient
	String getConfirmPassword();

	void setConfirmPassword(String confirmPassword);

	/**
	 * 关键字查询
	 * 
	 * @return
	 */
	@Transient
	String getKeywords();

	void setKeywords(String keywords);

	/**
	 * 角色ID查询
	 * 
	 * @return
	 */
	@Transient
	Long getRoleId();

	void setRoleId(Long roleId);

	@Column(name = "`user_roles`")
	@FieldDef(label = "关联查询的角色名称")
	@Transient
	String getUserRoles();

	void setUserRoles(String userRoles);

	@Column(name = "`position_name`")
	@FieldDef(label = "职位名称")
	@Transient
	String getPositionName();

	void setPositionName(String positionName);

	@Operator(Operator.IN)
	@Column(name = "`id`")
	List<String> getIds();

	void setIds(List<String> ids);

	@Operator(Operator.IN)
	@Column(name = "`firm_code`")
	List<String> getFirmCodes();

	void setFirmCodes(List<String> firmCodes);

	@Operator(Operator.GREAT_EQUAL_THAN)
	@Column(name = "`last_login_time`")
	Date getLastLoginTimeStart();

	void setLastLoginTimeStart(Date lastLoginTimeStart);

	@Operator(Operator.LITTLE_EQUAL_THAN)
	@Column(name = "`last_login_time`")
	Date getLastLoginTimeEnd();

	void setLastLoginTimeEnd(Date lastLoginTimeEnd);

	@Operator(Operator.GREAT_EQUAL_THAN)
	@Column(name = "`created`")
	Date getCreateTimeStart();

	void setCreateTimeStart(Date createTimeStart);

	@Operator(Operator.LITTLE_EQUAL_THAN)
	@Column(name = "`created`")
	Date getCreateTimeEnd();

	void setCreateTimeEnd(Date createTimeEnd);

}
