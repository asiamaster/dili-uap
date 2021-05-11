package com.dili.uap.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 客户端
 * This file was generated on 2021-03-08 12:45:30.
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
    @Like
    String getName();

    void setName(String name);

    @Column(name = "`code`")
    @FieldDef(label="唯一编码(openId)", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCode();

    void setCode(String code);

    @Column(name = "`secret`")
    @FieldDef(label="密钥", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSecret();

    void setSecret(String secret);
}