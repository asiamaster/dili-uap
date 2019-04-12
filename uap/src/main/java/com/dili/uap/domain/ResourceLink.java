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
 * This file was generated on 2019-04-11 15:00:26.
 */
@Table(name = "`resource_link`")
public interface ResourceLink extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`menu_id`")
    @FieldDef(label="内链ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getMenuId();

    void setMenuId(Long menuId);

    @Column(name = "`resource_id`")
    @FieldDef(label="资源ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getResourceId();

    void setResourceId(Long resourceId);
}