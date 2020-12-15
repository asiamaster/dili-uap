package com.dili.uap.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;

import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-12-01 15:23:34.
 */
@Table(name = "`position`")
public interface Position extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    @NotNull(message = "主要标识不能为空", groups = { ModifyView.class })
    Long getId();

    void setId(Long id);

    @Column(name = "`position_name`")
    @FieldDef(label="职位名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    @Size(max = 20, message = "职位名称长度介于0-20之间", groups = { AddView.class,ModifyView.class })
    @Like(Like.BOTH)
    String getPositionName();

    void setPositionName(String positionName);

    @Column(name = "`position_code`")
    @FieldDef(label="职位编码", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = true)
    @Size(max = 4, message = "职位编码长度介于0-4之间", groups = { AddView.class,ModifyView.class })
    String getPositionCode();

    void setPositionCode(String positionCode);

    @Column(name = "`description`")
    @FieldDef(label="描述", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Size(max = 255, message = "描述长度介于0-255之间", groups = { AddView.class,ModifyView.class })
    String getDescription();

    void setDescription(String description);

    @Column(name = "`firm_code`")
    @FieldDef(label="归属市场编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotNull(message = "所属市场不能为空", groups = { AddView.class,ModifyView.class })
    String getFirmCode();

    void setFirmCode(String firmCode);

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
}