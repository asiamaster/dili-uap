package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Menu;

import java.util.List;

public interface MenuMapper extends MyMapper<Menu> {

    /**
     * 查询用户拥有的菜单权限
     * @param userId
     * @return
     */
    List<Menu> findByUserId(Long userId);
}