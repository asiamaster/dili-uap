package com.dili.uap.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-18 11:15:40.
 */
@Table(name = "`system`")
public class System extends BaseDomain {
    /**
     * 系统标识
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 上级系统标识
     */
    @Column(name = "`parent_id`")
    private Long parentId;

    /**
     * 系统名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 系统编码
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 系统描述
     */
    @Column(name = "`description`")
    private String description;

    /**
     * 系统url，为空则跳到内部系统权限菜单，外部系统可定制
     */
    @Column(name = "`url`")
    private String url;

    /**
     * 图标URL
     */
    @Column(name = "`icon_url`")
    private String iconUrl;

    /**
     * 所属市场
     */
    @Column(name = "`firm_id`")
    private Long firmId;

    /**
     * 创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    /**
     * 类型##1:内部系统,2:外部系统
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 获取系统标识
     *
     * @return id - 系统标识
     */
    @FieldDef(label="系统标识")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置系统标识
     *
     * @param id 系统标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取上级系统标识
     *
     * @return parent_id - 上级系统标识
     */
    @FieldDef(label="上级系统标识")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级系统标识
     *
     * @param parentId 上级系统标识
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取系统名称
     *
     * @return name - 系统名称
     */
    @FieldDef(label="系统名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getName() {
        return name;
    }

    /**
     * 设置系统名称
     *
     * @param name 系统名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取系统编码
     *
     * @return code - 系统编码
     */
    @FieldDef(label="系统编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置系统编码
     *
     * @param code 系统编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取系统描述
     *
     * @return description - 系统描述
     */
    @FieldDef(label="系统描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDescription() {
        return description;
    }

    /**
     * 设置系统描述
     *
     * @param description 系统描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取系统url，为空则跳到内部系统权限菜单，外部系统可定制
     *
     * @return url - 系统url，为空则跳到内部系统权限菜单，外部系统可定制
     */
    @FieldDef(label="系统url，为空则跳到内部系统权限菜单，外部系统可定制", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getUrl() {
        return url;
    }

    /**
     * 设置系统url，为空则跳到内部系统权限菜单，外部系统可定制
     *
     * @param url 系统url，为空则跳到内部系统权限菜单，外部系统可定制
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取图标URL
     *
     * @return icon_url - 图标URL
     */
    @FieldDef(label="图标URL", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 设置图标URL
     *
     * @param iconUrl 图标URL
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * 获取所属市场
     *
     * @return firm_id - 所属市场
     */
    @FieldDef(label="所属市场")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getFirmId() {
        return firmId;
    }

    /**
     * 设置所属市场
     *
     * @param firmId 所属市场
     */
    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    /**
     * 获取创建时间
     *
     * @return created - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取修改时间
     *
     * @return modified - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getModified() {
        return modified;
    }

    /**
     * 设置修改时间
     *
     * @param modified 修改时间
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * 获取类型##1:内部系统,2:外部系统
     *
     * @return type - 类型##1:内部系统,2:外部系统
     */
    @FieldDef(label="类型")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型##1:内部系统,2:外部系统
     *
     * @param type 类型##1:内部系统,2:外部系统
     */
    public void setType(Integer type) {
        this.type = type;
    }
}