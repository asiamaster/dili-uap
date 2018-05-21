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
 * This file was generated on 2018-05-21 16:08:04.
 */
@Table(name = "`menu`")
public class Menu extends BaseDomain {
    /**
     * 主键
     */
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
     * 所属系统
     */
    @Column(name = "`system_id`")
    private Long systemId;

    /**
     * 排序号
     */
    @Column(name = "`order_number`")
    private Integer orderNumber;

    /**
     * 菜单url
     */
    @Column(name = "`url`")
    private String url;

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
     * 打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}
     */
    @Column(name = "`target`")
    private Integer target;

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
     * 类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 菜单图标
     */
    @Column(name = "`icon_cls`")
    private String iconCls;

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
     * 获取所属系统
     *
     * @return system_id - 所属系统
     */
    @FieldDef(label="所属系统")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getSystemId() {
        return systemId;
    }

    /**
     * 设置所属系统
     *
     * @param systemId 所属系统
     */
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    /**
     * 获取排序号
     *
     * @return order_number - 排序号
     */
    @FieldDef(label="排序号")
    @EditMode(editor = FieldEditor.Number, required = true)
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
     * 获取菜单url
     *
     * @return url - 菜单url
     */
    @FieldDef(label="菜单url", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getUrl() {
        return url;
    }

    /**
     * 设置菜单url
     *
     * @param url 菜单url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    @FieldDef(label="名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
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
    @EditMode(editor = FieldEditor.Text, required = true)
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
     * 获取打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}
     *
     * @return target - 打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}
     */
    @FieldDef(label="打开链接的位置")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"当前窗口\",\"value\":0},{\"text\":\"新开窗口\",\"value\":1}]}")
    public Integer getTarget() {
        return target;
    }

    /**
     * 设置打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}
     *
     * @param target 打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}
     */
    public void setTarget(Integer target) {
        this.target = target;
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
     * 获取类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}
     *
     * @return type - 类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}
     */
    @FieldDef(label="类型")
    @EditMode(editor = FieldEditor.Combo, required = true, params="{\"data\":[{\"text\":\"目录\",\"value\":0},{\"text\":\"链接\",\"value\":1}]}")
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}
     *
     * @param type 类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取菜单图标
     *
     * @return icon_cls - 菜单图标
     */
    @FieldDef(label="菜单图标", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getIconCls() {
        return iconCls;
    }

    /**
     * 设置菜单图标
     *
     * @param iconCls 菜单图标
     */
    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}