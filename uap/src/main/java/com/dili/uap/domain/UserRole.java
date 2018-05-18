package com.dili.uap.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-05-18 11:48:16.
 */
@Table(name = "`user_role`")
public class UserRole extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id##外键
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 角色id##外键
     */
    @Column(name = "`role_id`")
    private Long roleId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户id##外键
     *
     * @return user_id - 用户id##外键
     */
    @FieldDef(label="用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id##外键
     *
     * @param userId 用户id##外键
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取角色id##外键
     *
     * @return role_id - 角色id##外键
     */
    @FieldDef(label="角色id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id##外键
     *
     * @param roleId 角色id##外键
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}