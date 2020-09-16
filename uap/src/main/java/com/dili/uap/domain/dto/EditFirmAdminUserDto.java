package com.dili.uap.domain.dto;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;

public interface EditFirmAdminUserDto extends IDTO {

	Long getId();

	void setId(Long id);

	@Column(name = "`user_name`")
	@FieldDef(label = "用户名", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = true)
	@NotNull(message = "用户名不能为空", groups = { AddView.class })
	@Size(min = 2, max = 20, message = "用户名称长度介于2-20之间", groups = { AddView.class })
	String getUserName();

	void setUserName(String userName);

	@Column(name = "`cellphone`")
	@FieldDef(label = "手机号码", maxLength = 12)
	@EditMode(editor = FieldEditor.Text)
	@NotNull(message = "手机号码不能为空", groups = { AddView.class, ModifyView.class })
	@Pattern(regexp = "^1[3-8]\\d{9}$", message = "手机号格式不正确", groups = { AddView.class, ModifyView.class })
	String getCellphone();

	void setCellphone(String cellphone);

	@Column(name = "`email`")
	@FieldDef(label = "邮箱", maxLength = 40)
	@EditMode(editor = FieldEditor.Text)
	@NotNull(message = "邮箱不能为空", groups = { AddView.class, ModifyView.class })
	@Size(max = 64, message = "邮箱长度不能超过64个字符", groups = { AddView.class, ModifyView.class })
	String getEmail();

	@Column(name = "`password`")
	@FieldDef(label = "密码", maxLength = 8)
	@EditMode(editor = FieldEditor.Text)
	@NotNull(message = "密码不能为空", groups = { AddView.class, ModifyView.class })
	@Size(min = 0, max = 8, message = "密码长度介于0-8之间", groups = { AddView.class })
	String getPassword();

	void setPassword(String password);

	void setEmail(String email);

	List<String> getResourceIds();

	void setResourceIds(List<String> resourceIds);

	Long getUserId();

	void setUserId(Long userId);

	Long getRoleId();

	void setRoleId(Long roleId);
}
