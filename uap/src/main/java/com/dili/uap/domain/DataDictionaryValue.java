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
 * This file was generated on 2018-05-21 10:40:13.
 */
@Table(name = "`data_dictionary_value`")
public class DataDictionaryValue extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 上级id
     */
    @Column(name = "`parent_id`")
    private Long parentId;

    /**
     * 数据字典编码
     */
    @Column(name = "`dd_code`")
    private String ddCode;

    /**
     * 排序号
     */
    @Column(name = "`order_number`")
    private Integer orderNumber;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 编码
     */
    @Column(name = "`code`")
    private String code;

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
     * 获取上级id
     *
     * @return parent_id - 上级id
     */
    @FieldDef(label="上级id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级id
     *
     * @param parentId 上级id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取数据字典编码
     *
     * @return dd_code - 数据字典编码
     */
    @FieldDef(label="数据字典编码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDdCode() {
        return ddCode;
    }

    /**
     * 设置数据字典编码
     *
     * @param ddCode 数据字典编码
     */
    public void setDdCode(String ddCode) {
        this.ddCode = ddCode;
    }

    /**
     * 获取排序号
     *
     * @return order_number - 排序号
     */
    @FieldDef(label="排序号")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置排序号
     *
     * @param orderNumber 排序号
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    @FieldDef(label="名称", maxLength = 40)
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
     * 获取编码
     *
     * @return code - 编码
     */
    @FieldDef(label="编码", maxLength = 40)
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
}