package com.dili.uap.sdk.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-22 11:24:31.
 */
@Table(name = "`role`")
public interface Role extends IBaseDomain, IMybatisForceParams {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "主键")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`role_name`")
	@FieldDef(label = "角色名", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = true)
	@Like
	@NotNull(message = "角色名不能为空")
	@Size(min = 2, max = 20, message = "角色名称长度介于2-20之间")
	String getRoleName();

	void setRoleName(String roleName);

	@Column(name = "`description`")
	@FieldDef(label = "角色描述", maxLength = 255)
	@EditMode(editor = FieldEditor.Text)
	@Size(max = 20, message = "角色描述长度不能超过20个字符")
	String getDescription();

	void setDescription(String description);

	@Column(name = "`firm_code`")
	@FieldDef(label = "所属市场编码", maxLength = 50)
	@EditMode(editor = FieldEditor.Text)
	String getFirmCode();

	void setFirmCode(String firmCode);

	@Column(name = "`created`")
	@FieldDef(label = "创建时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getCreated();

	void setCreated(Date created);

	@Column(name = "`modified`")
	@FieldDef(label = "修改时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getModified();

	void setModified(Date modified);

	@Column(name = "`state`")
	@FieldDef(label = "状态")
	@EditMode(editor = FieldEditor.Number, required = true)
	Integer getState();

	void setState(Integer state);
	
	@Column(name = "`parent_id`")
	@FieldDef(label = "父级角色")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getParentId();

	void setParentId(Long parentId);
}