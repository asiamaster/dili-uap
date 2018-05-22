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
 * This file was generated on 2018-05-18 11:45:40.
 */
@Table(name = "`role`")
public class Role extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名
     */
    @Column(name = "`role_name`")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "`description`")
    private String description;

    /**
     * 所属市场
     */
    @Column(name = "`firm_code`")
    private String firmCode;

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
     * 获取主键
     *
     * @return id - 主键
     */
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取角色名
     *
     * @return role_name - 角色名
     */
    @FieldDef(label="角色名", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名
     *
     * @param roleName 角色名
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色描述
     *
     * @return description - 角色描述
     */
    @FieldDef(label="角色描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getDescription() {
        return description;
    }

    /**
     * 设置角色描述
     *
     * @param description 角色描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取所属市场
     *
     * @return firm_id - 所属市场
     */
    @FieldDef(label="所属市场")
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getFirmCode() {
        return firmCode;
    }

    /**
     * 设置所属市场
     *
     * @param firmCode 所属市场
     */
    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
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
}