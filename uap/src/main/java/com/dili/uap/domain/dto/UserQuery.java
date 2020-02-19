package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.validator.AddView;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table(name = "`user`")
public interface UserQuery extends User {

    @Column(name = "`user_name`")
    @FieldDef(label="用户名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotNull(message = "用户名不能为空", groups = {AddView.class})
    @Size(min = 2, max = 20, message = "用户名称长度介于2-20之间", groups = {AddView.class})
    @Like
    @Override
    String getUserName();
    @Override
    void setUserName(String userName);
}
