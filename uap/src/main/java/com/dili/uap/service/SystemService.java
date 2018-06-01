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
     * 检查并新增系统
     * @param system
     * @return
     */
    BaseOutput<Object>  insertAfterCheck(System system);
    /**
     * 检查并修改系统
     * @param system
     * @return
     */
    BaseOutput<Object>  updateAfterCheck(System system);
    
    /**
     * 检查并删除系统信息
     * @param id主键
     * @return
     */
   BaseOutput<Object> deleteAfterCheck(Long id);
}