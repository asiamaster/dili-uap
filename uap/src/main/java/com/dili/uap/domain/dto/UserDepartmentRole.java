package com.dili.uap.domain.dto;

import java.util.Date;
import java.util.List;
import com.dili.ss.domain.BaseDomain;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Role;

public class UserDepartmentRole extends BaseDomain  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1562069406116228728L;

	private Department department;
	private List<Role> roles;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	private Long id;

	private String userName;

	private String password;

	private String firmCode;

	private Long departmentId;
	
	private String position;

	private String cardNumber;

	private Date locked;

	private Date created;

	private Date modified;

	private Integer state;

	private String realName;

	private String serialNumber;

	private String cellphone;
	
	private String email;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirmCode() {
		return firmCode;
	}

	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getLocked() {
		return locked;
	}

	public void setLocked(Date locked) {
		this.locked = locked;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	

}
