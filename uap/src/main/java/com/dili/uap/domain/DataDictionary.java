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
 * This file was generated on 2018-05-21 10:39:45.
 */
@Table(name = "`data_dictionary`")
public class DataDictionary extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 编码
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 描述
     */
    @Column(name = "`description`")
    private String description;

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
     * 是否可用##{provider:"YnProvider"}
     */
    @Column(name = "`yn`")
    private Integer yn;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取编码
     *
     * @return code - 编码
     */
    @FieldDef(label="编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    @FieldDef(label="名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    @FieldDef(label="描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取是否可用##{provider:"YnProvider"}
     *
     * @return yn - 是否可用##{provider:"YnProvider"}
     */
    @FieldDef(label="是否可用")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"YnProvider\"}")
    public Integer getYn() {
        return yn;
    }

    /**
     * 设置是否可用##{provider:"YnProvider"}
     *
     * @param yn 是否可用##{provider:"YnProvider"}
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }
}