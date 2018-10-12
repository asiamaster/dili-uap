package com.dili.uap.sdk.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 *
 * This file was generated on 2018-05-23 16:17:38.
 */
@Table(name = "`user`")
public interface User extends IBaseDomain,IMybatisForceParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    @NotNull(message = "主要标识不能为空",groups = {ModifyView.class})
    Long getId();

    void setId(Long id);

    @Column(name = "`user_name`")
    @FieldDef(label="用户名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotNull(message = "用户名不能为空", groups = {AddView.class})
    @Size(min = 2, max = 20, message = "用户名称长度介于2-20之间", groups = {AddView.class})
    String getUserName();

    void setUserName(String userName);

    @Column(name = "`password`")
    @FieldDef(label="密码", maxLength = 128)
    @EditMode(editor = FieldEditor.Text)
    String getPassword();

    void setPassword(String password);

    @Column(name = "`firm_code`")
    @FieldDef(label="归属市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text)
    @NotNull(message = "所属市场不能为空", groups = {AddView.class})
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`department_id`")
    @FieldDef(label="归属部门")
    @EditMode(editor = FieldEditor.Number)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`position`")
    @FieldDef(label="职位", maxLength = 20)
    @EditMode(editor = FieldEditor.Text)
    @Size(max = 20, message = "职位长度不能超过20个字符", groups = {AddView.class, ModifyView.class})
    String getPosition();

    void setPosition(String position);

    @Column(name = "`card_number`")
    @FieldDef(label="卡号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text)
    @Size(min = 12, max = 22, message = "卡号长度介于12-22之间", groups = {AddView.class, ModifyView.class})
    String getCardNumber();

    void setCardNumber(String cardNumber);

    @Column(name = "`locked`")
    @FieldDef(label="锁定时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLocked();

    void setLocked(Date locked);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`state`")
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getState();

    void setState(Integer state);

    @Column(name = "`real_name`")
    @FieldDef(label="真实姓名", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    @Like
    @NotNull(message = "真实姓名不能为空", groups = {AddView.class, ModifyView.class})
    @Size(min = 2, max = 8, message = "真实姓名长度介于2-8之间", groups = {AddView.class, ModifyView.class})
    String getRealName();

    void setRealName(String realName);

    @Column(name = "`serial_number`")
    @FieldDef(label="用户编号", maxLength = 128)
    @EditMode(editor = FieldEditor.Text)
    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    @Column(name = "`cellphone`")
    @FieldDef(label="手机号码", maxLength = 24)
    @EditMode(editor = FieldEditor.Text)
    @NotNull(message = "手机号码不能为空",groups = {AddView.class,ModifyView.class})
    @Pattern(regexp = "^1[3-8]\\d{9}$",message = "手机号格式不正确",groups = {AddView.class,ModifyView.class})
    String getCellphone();

    void setCellphone(String cellphone);

    @Column(name = "`email`")
    @FieldDef(label="邮箱", maxLength = 64)
    @EditMode(editor = FieldEditor.Text)
    @NotNull(message = "邮箱不能为空",groups = {AddView.class,ModifyView.class})
    @Size(max = 64,message = "邮箱长度不能超过64个字符",groups = {AddView.class,ModifyView.class})
    String getEmail();

    void setEmail(String email);

    @Column(name = "`description`")
    @FieldDef(label="备注", maxLength = 255)
    @EditMode(editor = FieldEditor.Text)
    @Size(max = 20,message = "备注长度不能超过20个字符",groups = {AddView.class,ModifyView.class})
    String getDescription();

    void setDescription(String description);
}