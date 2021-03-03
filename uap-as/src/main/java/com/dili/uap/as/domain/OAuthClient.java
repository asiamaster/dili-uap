package com.dili.uap.as.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 客户端
 * This file was generated on 2021-02-26 14:13:22.
 */
@Table(name = "`oauth_client`")
public interface OAuthClient extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getName();

    void setName(String name);

    @Column(name = "`code`")
    @FieldDef(label="编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCode();

    void setCode(String code);

    @Column(name = "`secret`")
    @FieldDef(label="密钥", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSecret();

    void setSecret(String secret);
}