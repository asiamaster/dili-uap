package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
public interface MenuService extends BaseService<Menu, Long> {

    /**
     * 根据用户id和系统编码查询目录和链接菜单权限
     * @param jsonParam : userId和systemCode
     * @return
     */
    List<Menu> listDirAndLinksByUserIdAndSystemCode(String jsonParam);

    /**
     * 根据用户id和系统编码查询目录和链接菜单权限
     * @param userId
     * @param systemCode
     * @return
     */
    List<Map> listDirAndLinks(Long userId, String systemCode);

    /**
     * 根据id获取上级菜单
     * @param id
     * @return
     */
    List<Menu> getParentMenus(String id);

    /**
     * 根据url获取上级菜单
     * @param url
     * @return
     */
    List<Menu> getParentMenusByUrl(String url);

    /**
     * 根据url获取菜单详情，包含系统信息
     * @param url
     * @return
     */
    Map<String, Object> getMenuDetailByUrl(String url);

    /**
     * 列出所有系统和菜单
     * @return
     */
    List<Map> listSystemMenu();

    /**
     * 删除菜单节点，判断下面是否有子菜单和资源，成功返回null，失败返回错误信息
     * @param id
     * @return
     */
    String deleteMenu(Long id);
}