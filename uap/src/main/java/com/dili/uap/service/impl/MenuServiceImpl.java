package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    public MenuMapper getActualDao() {
        return (MenuMapper)getDao();
    }
}