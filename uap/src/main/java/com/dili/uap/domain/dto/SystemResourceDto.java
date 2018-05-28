package com.dili.uap.domain.dto;

import com.dili.ss.dto.IBaseDomain;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/24 14:45
 */
public interface SystemResourceDto extends IBaseDomain {

    /**
     * 数据ID
     * @return
     */
    String getTreeId();
    void setTreeId(String treeId);

    /**
     * 父级ID
     * @return
     */
    String getParentId();
    void setParentId(String parentId);

    /**
     * 节点名称
     * @return
     */
    String getName();
    void setName(String name);

    /**
     * 系统ID
     * @return
     */
    Long getSystemId();
    void setSystemId(Long systemId);

    /**
     * 资源描述
     * @return
     */
    String getDescription();
    void setDescription(String description);

    /**
     * 是否是菜单
     * @return
     */
    Integer getMenu();
    void setMenu(Long menu);

    /**
     * 节点状态，'open' 或 'closed'，如果为'closed'的时候，将不自动展开该节点。
     * @return
     */
    String getState();
    void setState(String state);

    /**
     * 节点是否选中
     * @return
     */
    Boolean getChecked();
    void setChecked(Boolean checked);

    /**
     * 权限类型(目录，链接，内链和资源)
     * @return
     */
    String getType();
    void setType(String type);
    
}
