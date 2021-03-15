package com.dili.uap.as.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 开放授权客户端权限
 * This file was generated on 2021-03-10 15:33:44.
 */
@Table(name = "`oauth_client_privilege`")
public interface OauthClientPrivilege extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`oauth_client_id`")
    @FieldDef(label="oauthClientId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getOauthClientId();

    void setOauthClientId(Long oauthClientId);

    @Column(name = "`privilege`")
    @FieldDef(label="privilege")
    @EditMode(editor = FieldEditor.Number, required = true)
//    @Operator(Operator.IN)
    Integer getPrivilege();

    void setPrivilege(Integer privilege);

    @Column(name = "`user_id`")
    @FieldDef(label="用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserId();

    void setUserId(Long userId);
}