package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public MenuMapper getActualDao() {
        return (MenuMapper)getDao();
    }

    @Override
    public List<Menu> findDirAndLinksByUserId(String userId){
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("用户id为空");
        }
        return this.menuMapper.findDirAndLinksByUserId(Long.valueOf(userId));
    }
}