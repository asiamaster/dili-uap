package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.uap.domain.Menu;
import com.dili.uap.sdk.exception.NotLoginException;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
public interface MenuService extends BaseService<Menu, Long> {

    /**
     * 根据用户id查询目录和链接菜单权限
     * @param userId
     * @return
     */
    List<Menu> findDirAndLinksByUserId(String userId);

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
}