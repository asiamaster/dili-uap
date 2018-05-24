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
 * This file was generated on 2018-05-24 17:44:18.
 */
@Table(name = "`system_config`")
public interface SystemConfig extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`code`")
    @FieldDef(label="编码", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`value`")
    @FieldDef(label="值", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getValue();

    void setValue(String value);

    @Column(name = "`description`")
    @FieldDef(label="描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDescription();

    void setDescription(String description);

    @Column(name = "`system_code`")
    @FieldDef(label="所属系统编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSystemCode();

    void setSystemCode(String systemCode);

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

    @Column(name = "`yn`")
    @FieldDef(label="是否可用")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"YnProvider\"}")
    Integer getYn();

    void setYn(Integer yn);
}