package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.System;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:24:56.
 */
public interface SystemService extends BaseService<System, Long> {
    /**
     * 新增系统检查
     * @param system
     * @return
     */
    BaseOutput<Object>  insertAfterCheck(System system);
    /**
     * 修改系统检查
     * @param system
     * @return
     */
    BaseOutput<Object>  updateAfterCheck(System system);
}