package com.dili.uap.domain;

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
 * 
 * This file was generated on 2018-05-25 15:04:04.
 */
@Table(name = "`role_resource`")
public interface RoleResource extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`role_id`")
    @FieldDef(label="角色ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getRoleId();

    void setRoleId(Long roleId);

    @Column(name = "`resource_id`")
    @FieldDef(label="资源ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getResourceId();

    void setResourceId(Long resourceId);
}