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
 * This file was generated on 2018-05-22 15:27:04.
 */
@Table(name = "`system_exception_log`")
public interface SystemExceptionLog extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`ip`")
    @FieldDef(label="服务器IP", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIp();

    void setIp(String ip);

    @Column(name = "`system_id`")
    @FieldDef(label="系统")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSystemId();

    void setSystemId(Long systemId);

    @Column(name = "`menu_id`")
    @FieldDef(label="功能模块")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMenuId();

    void setMenuId(Long menuId);

    @Column(name = "`type`")
    @FieldDef(label="异常类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getType();

    void setType(Integer type);

    @Column(name = "`firm_code`")
    @FieldDef(label="市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`version`")
    @FieldDef(label="版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getVersion();

    void setVersion(Integer version);

    @Column(name = "`exception_time`")
    @FieldDef(label="异常时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getExceptionTime();

    void setExceptionTime(Date exceptionTime);

    @Column(name = "`msg`")
    @FieldDef(label="异常信息", maxLength = 511)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMsg();

    void setMsg(String msg);
}