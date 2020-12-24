package com.dili.uap.sdk.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-22 16:10:05.
 */
@Table(name = "`department`")
public interface Department extends IBaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`parent_id`")
	@FieldDef(label = "上级部门id")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getParentId();

	void setParentId(Long parentId);

	@Column(name = "`firm_code`")
	@FieldDef(label = "所属市场", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getFirmCode();

	void setFirmCode(String firmCode);

	@Like(Like.BOTH)
	@Column(name = "`name`")
	@FieldDef(label = "名称", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getName();

	void setName(String name);

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

	@Column(name = "`created_id`")
	@FieldDef(label = "创建人id")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getCreatedId();

	void setCreatedId(Long createdId);

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

	@Column(name = "`modified_id`")
	@FieldDef(label = "修改人id")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getModifiedId();

	void setModifiedId(Long modifiedId);
}