package com.dili.uap.sdk.domain;

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
 * This file was generated on 2020-10-21 10:18:50.
 */
@Table(name = "`user_push_info`")
public interface UserPushInfo extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_id`")
    @FieldDef(label="用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`push_id`")
    @FieldDef(label="推送id", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPushId();

    void setPushId(String pushId);

    @Column(name = "`platform`")
    @FieldDef(label="平台，androi，ios", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPlatform();

    void setPlatform(String platform);
}