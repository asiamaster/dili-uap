package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Menu;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends MyMapper<Menu> {

    /**
     * 根据用户id查询用户拥有的所有菜单权限
     * @param userId
     * @return
     */
    List<Menu> listByUserId(Long userId);

    /**
     * 根据用户id查询目录和链接菜单权限
     * @param param key为userId和systemCode
     * @return
     */
    List<Menu> listDirAndLinksByUserId(Map param);

    /**
     * 获取上级菜单的id列表，以逗号分隔
     * @param id
     * @return
     */
    String getParentMenus(String id);

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
}