package com.dili.uap.sdk.domain.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

public interface ClientMenuDto extends IDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "主键")
	@EditMode(editor = FieldEditor.Number, required = true)
	String getId();

	void setId(String id);

	@Column(name = "`system_id`")
	@FieldDef(label = "所属系统")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getSystemId();

	void setSystemId(Long systemId);

	@Column(name = "`parent_id`")
	@FieldDef(label = "上级id")
	@EditMode(editor = FieldEditor.Number, required = false)
	String getParentId();

	void setParentId(String parentId);

	@Column(name = "`order_number`")
	@FieldDef(label = "排序号")
	@EditMode(editor = FieldEditor.Number, required = true)
	Integer getOrderNumber();

	void setOrderNumber(Integer orderNumber);

	@Column(name = "`url`")
	@FieldDef(label = "菜单url", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getUrl();

	void setUrl(String url);

	@Column(name = "`name`")
	@FieldDef(label = "名称", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getName();

	void setName(String name);

	@Column(name = "`description`")
	@FieldDef(label = "描述", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getDescription();

	void setDescription(String description);

	@Column(name = "`target`")
	@FieldDef(label = "打开链接的位置")
	@EditMode(editor = FieldEditor.Combo, required = false, params = "{\"data\":[{\"text\":\"当前窗口\",\"value\":0},{\"text\":\"新开窗口\",\"value\":1}]}")
	Integer getTarget();

	void setTarget(Integer target);

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

	@Column(name = "`type`")
	@FieldDef(label = "类型")
	@EditMode(editor = FieldEditor.Combo, required = true, params = "{\"data\":[{\"text\":\"目录\",\"value\":0},{\"text\":\"链接\",\"value\":1}]}")
	Integer getType();

	void setType(Integer type);

	@Column(name = "`icon_cls`")
	@FieldDef(label = "菜单图标", maxLength = 40)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getIconCls();

	void setIconCls(String iconCls);

	@Column(name = "`shortcut`")
	@FieldDef(label = "快捷菜单")
	Integer getShortcut();

	void setShortcut(Integer shortcut);

	@Column(name = "`index_url`")
	@FieldDef(label="系统首页url", maxLength = 255)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getIndexUrl();

	void setIndexUrl(String indexUrl);

	@Column(name = "`is_frontend`")
	@FieldDef(label="是否前端")
	@EditMode(editor = FieldEditor.Combo, required = false)
	Integer getIsFrontend();
	void setIsFrontend(Integer frontend);


}
