package com.dili.uap.sdk.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-07-09 12:26:49.
 */
@Table(name = "`user_data_auth`")
public interface UserDataAuth extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`ref_code`")
    @FieldDef(label="数据权限引用编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getRefCode();

    void setRefCode(String refCode);

    @Column(name = "`user_id`")
    @FieldDef(label="用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`value`")
    @FieldDef(label="数据权限值", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getValue();

    void setValue(String value);
}