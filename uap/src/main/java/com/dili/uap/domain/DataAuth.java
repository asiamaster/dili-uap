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
 * This file was generated on 2018-05-23 11:29:55.
 */
@Table(name = "`data_auth`")
public interface DataAuth extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getName();

    void setName(String name);

    @Column(name = "`type`")
    @FieldDef(label="数据权限分类", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getType();

    void setType(String type);

    @Column(name = "`data_id`")
    @FieldDef(label="数据权限ID", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDataId();

    void setDataId(String dataId);

    @Column(name = "`parent_data_id`")
    @FieldDef(label="父级数据ID", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getParentDataId();

    void setParentDataId(String parentDataId);

    @Column(name = "`description`")
    @FieldDef(label="描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDescription();

    void setDescription(String description);
}