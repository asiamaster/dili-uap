package com.dili.uap.domain;

import com.dili.ss.dto.IBaseDomain;
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
 * This file was generated on 2018-05-28 11:33:04.
 */
@Table(name = "`login_log`")
public interface LoginLog extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_id`")
    @FieldDef(label="用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`ip`")
    @FieldDef(label="编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIp();

    void setIp(String ip);

    @Column(name = "`host`")
    @FieldDef(label="备注", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getHost();

    void setHost(String host);

    @Column(name = "`login_time`")
    @FieldDef(label="操作时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLoginTime();

    void setLoginTime(Date loginTime);

    @Column(name = "`type`")
    @FieldDef(label="操作类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getType();

    void setType(Integer type);

    @Column(name = "`system_code`")
    @FieldDef(label="系统编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSystemCode();

    void setSystemCode(String systemCode);

    @Column(name = "`system_name`")
    @FieldDef(label="系统名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSystemName();

    void setSystemName(String systemName);

    @Column(name = "`firm_code`")
    @FieldDef(label="市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`firm_name`")
    @FieldDef(label="市场名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmName();

    void setFirmName(String firmName);

    @Column(name = "`success`")
    @FieldDef(label="是否成功")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSuccess();

    void setSuccess(Integer success);

    @Column(name = "`msg`")
    @FieldDef(label="结果信息", maxLength = 511)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMsg();

    void setMsg(String msg);
}