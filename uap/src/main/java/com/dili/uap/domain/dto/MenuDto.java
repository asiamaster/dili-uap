package com.dili.uap.domain.dto;

import com.dili.uap.domain.Menu;
import com.dili.uap.domain.Resource;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/24 15:25
 */
public interface MenuDto extends Menu {

    List<Resource> getResources();

    void setResources(List<Resource> resources);
}
