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
 * This file was generated on 2018-05-22 15:30:02.
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
    @FieldDef(label="用户id", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getUserId();

    void setUserId(String userId);

    @Column(name = "`ip`")
    @FieldDef(label="IP地址", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIp();

    void setIp(String ip);

    @Column(name = "`host`")
    @FieldDef(label="主机名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getHost();

    void setHost(String host);

    @Column(name = "`login_time`")
    @FieldDef(label="登录时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLoginTime();

    void setLoginTime(Date loginTime);

    @Column(name = "`logout_time`")
    @FieldDef(label="登出时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getLogoutTime();

    void setLogoutTime(Date logoutTime);

    @Column(name = "`system_id`")
    @FieldDef(label="登录系统")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSystemId();

    void setSystemId(Long systemId);

    @Column(name = "`firm_code`")
    @FieldDef(label="市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`success`")
    @FieldDef(label="是否成功")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSuccess();

    void setSuccess(Integer success);

    @Column(name = "`reason`")
    @FieldDef(label="失败原因", maxLength = 511)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getReason();

    void setReason(String reason);
}