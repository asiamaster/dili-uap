package com.dili.uap.sdk.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-22 16:24:56.
 */
@Table(name = "`system`")
public interface System extends IBaseDomain,IMybatisForceParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="系统标识")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`parent_id`")
    @FieldDef(label="上级系统标识")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`name`")
    @FieldDef(label="系统名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`code`")
    @FieldDef(label="系统编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`description`")
    @FieldDef(label="系统描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDescription();

    void setDescription(String description);

    @Column(name = "`url`")
    @FieldDef(label="系统url，为空则跳到内部系统权限菜单，外部系统可定制", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUrl();

    void setUrl(String url);

    @Column(name = "`index_url`")
    @FieldDef(label="系统首页url", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIndexUrl();

    void setIndexUrl(String indexUrl);

    @Column(name = "`icon_url`")
    @FieldDef(label="图标URL", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIconUrl();

    void setIconUrl(String iconUrl);

    @Column(name = "`firm_code`")
    @FieldDef(label="所属市场", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`logo_url`")
    @FieldDef(label="LOGO URL", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLogoUrl();

    void setLogoUrl(String logoUrl);

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

    @Column(name = "`type`")
    @FieldDef(label="类型")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getType();

    void setType(Integer type);

    @Column(name = "`order_number`")
    @FieldDef(label="排序号")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getOrderNumber();

    void setOrderNumber(Integer orderNumber);
}