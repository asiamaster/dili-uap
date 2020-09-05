package com.dili.uap.sdk.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-23 14:31:07.
 */
@Table(name = "`firm`")
public interface Firm extends IBaseDomain, IMybatisForceParams {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`name`")
	@FieldDef(label = "名称", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getName();

	void setName(String name);

	@Column(name = "`simple_name`")
	@FieldDef(label = "简称", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getSimpleName();

	void setSimpleName(String simpleName);

	@Column(name = "`parent_id`")
	@FieldDef(label = "上级id")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getParentId();

	void setParentId(Long parentId);

	@Column(name = "`code`")
	@FieldDef(label = "编号", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getCode();

	void setCode(String code);

	@Column(name = "`description`")
	@FieldDef(label = "描述", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getDescription();

	void setDescription(String description);

	@Column(name = "`created`")
	@FieldDef(label = "创建时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getCreated();

	void setCreated(Date created);

	@Column(name = "`modified`")
	@FieldDef(label = "操作时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getModified();

	void setModified(Date modified);

	@Column(name = "`industry`")
	@FieldDef(label = "所属行业", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getIndustry();

	void setIndustry(String industry);

	@Column(name = "`certificate_type`")
	@FieldDef(label = "证件类型", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getCertificateType();

	void setCertificateType(String certificateType);

	@Column(name = "`certificate_number`")
	@FieldDef(label = "证件号", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getCertificateNumber();

	void setCertificateNumber(String certificateNumber);

	@Column(name = "`effect_time`")
	@FieldDef(label = "生效时间")
	@EditMode(editor = FieldEditor.Date, required = false)
	Date getEffectTime();

	void setEffectTime(Date effectTime);

	@Column(name = "`failure_time`")
	@FieldDef(label = "失效时间")
	@EditMode(editor = FieldEditor.Date, required = false)
	Date getFailureTime();

	void setFailureTime(Date failureTime);

	@Column(name = "`registered_province_id`")
	@FieldDef(label = "注册所在省")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getRegisteredProvinceId();

	void setRegisteredProvinceId(Long registeredProvinceId);

	@Column(name = "`registered_city_id`")
	@FieldDef(label = "注册所在城市")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getRegisteredCityId();

	void setRegisteredCityId(Long registeredCityId);

	@Column(name = "`registered_district_id`")
	@FieldDef(label = "注册所在区")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getRegisteredDistrictId();

	void setRegisteredDistrictId(Long registeredDistrictId);

	@Column(name = "`registered_detail_address`")
	@FieldDef(label = "注册详细地址", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getRegisteredDetailAddress();

	void setRegisteredDetailAddress(String registeredDetailAddress);

	@Column(name = "`actual_province_id`")
	@FieldDef(label = "实际所在省")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getActualProvinceId();

	void setActualProvinceId(Long actualProvinceId);

	@Column(name = "`actual_city_id`")
	@FieldDef(label = "实际所在市")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getActualCityId();

	void setActualCityId(Long actualCityId);

	@Column(name = "`actual_district_id`")
	@FieldDef(label = "实际所在区")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getActualDistrictId();

	void setActualDistrictId(Long actualDistrictId);

	@Column(name = "`actual_detail_address`")
	@FieldDef(label = "实际详细地址", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getActualDetailAddress();

	void setActualDetailAddress(String actualDetailAddress);

	@Column(name = "`legal_person_name`")
	@FieldDef(label = "法人名称", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getLegalPersonName();

	void setLegalPersonName(String legalPersonName);

	@Column(name = "`legal_person_certificate_type`")
	@FieldDef(label = "法人证件类型", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getLegalPersonCertificateType();

	void setLegalPersonCertificateType(String legalPersonCertificateType);

	@Column(name = "`legal_person_certificate_number`")
	@FieldDef(label = "法人证件号", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getLegalPersonCertificateNumber();

	void setLegalPersonCertificateNumber(String legalPersonCertificateNumber);

	@Column(name = "`certificate_validity_period`")
	@FieldDef(label = "法人证件有效期")
	@EditMode(editor = FieldEditor.Date, required = false)
	Date getCertificateValidityPeriod();

	void setCertificateValidityPeriod(Date certificateValidityPeriod);

	@Column(name = "`long_term_effictive`")
	@FieldDef(label = "法人证件是否长期有效")
	@EditMode(editor = FieldEditor.Number, required = false)
	Boolean getLongTermEffictive();

	void setLongTermEffictive(Boolean longTermEffictive);

	@Column(name = "`deposit_bank`")
	@FieldDef(label = "开户行")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getDepositBank();

	void setDepositBank(Long depositBank);

	@Column(name = "`deposit_bank_union_info_id`")
	@FieldDef(label = "联行信息id")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getDepositBankUnionInfoId();

	void setDepositBankUnionInfoId(Long depositBankUnionInfoId);

	@Column(name = "`bank_account`")
	@FieldDef(label = "银行账号", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getBankAccount();

	void setBankAccount(String bankAccount);

	@Column(name = "`telephone`")
	@FieldDef(label = "电话", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getTelephone();

	void setTelephone(String telephone);

	@Column(name = "`email`")
	@FieldDef(label = "邮箱", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getEmail();

	void setEmail(String email);

	@Column(name = "`state`")
	@FieldDef(label = "状态：1开通，2关闭")
	@EditMode(editor = FieldEditor.Number, required = true)
	Integer getState();

	void setState(Integer state);

	@Column(name = "`user_id`")
	@FieldDef(label = "管理员用户id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getUserId();

	void setUserId(Long userId);

	@Column(name = "`roleId`")
	@FieldDef(label = "管理员角色")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getRoleId();

	void setRoleId(Long roleId);

	List<String> getNames();

	void setNames(List<String> names);

	List<User> getUsers();

	void setUsers(List<User> users);

	User getUser();

	void setUser(User user);
}