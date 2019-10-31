package com.dili.uap.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-04-15 15:55:32.
 */
@Table(name = "`resource_link`")
public interface ResourceLink extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`resource_code`")
    @FieldDef(label="resourceCode", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getResourceCode();

    void setResourceCode(String resourceCode);

    @Column(name = "`menu_id`")
    @FieldDef(label="menuId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMenuId();

    void setMenuId(Long menuId);
}