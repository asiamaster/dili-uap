package com.dili.uap.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    Long getId();

    void setId(Long id);

    @Column(name = "`user_name`")
    @FieldDef(label="用户名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getUserName();

    void setUserName(String userName);

    @Column(name = "`password`")
    @FieldDef(label="密码", maxLength = 128)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPassword();

    void setPassword(String password);

    @Column(name = "`firm_code`")
    @FieldDef(label="归属市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`department_id`")
    @FieldDef(label="归属部门")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`position`")
    @FieldDef(label="职位", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPosition();

    void setPosition(String position);

    @Column(name = "`card_number`")
    @FieldDef(label="卡号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNumber();

    void setCardNumber(String cardNumber);

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
    String getRealName();

    void setRealName(String realName);

    @Column(name = "`serial_number`")
    @FieldDef(label="用户编号", maxLength = 128)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    @Column(name = "`cellphone`")
    @FieldDef(label="手机号码", maxLength = 24)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCellphone();

    void setCellphone(String cellphone);

    @Column(name = "`email`")
    @FieldDef(label="邮箱", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getEmail();

    void setEmail(String email);

    @Column(name = "`description`")
    @FieldDef(label="备注", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDescription();

    void setDescription(String description);
}