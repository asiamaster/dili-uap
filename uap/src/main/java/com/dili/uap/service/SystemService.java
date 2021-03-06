package com.dili.uap.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Systems;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:24:56.
 */
public interface SystemService extends BaseService<Systems, Long> {
    /**
     * 检查并新增系统
     * @param system
     * @return
     */
    BaseOutput<Object>  insertAfterCheck(Systems system);
    /**
     * 检查并修改系统
     * @param system
     * @return
     */
    BaseOutput<Object>  updateAfterCheck(Systems system);
    
    /**
     * 检查并删除系统信息
     * @param id 主键
     * @return
     */
   BaseOutput<Object> deleteAfterCheck(Long id);

    /**
     * 根据用户id查询其有权限的系统
     * @param userId
     * @return
     */
    List<Systems> listByUserId(Long userId);

    /**
     * 根据上下文中用户id查询其有权限的系统
     * @param param 首页系统菜单栏menubutton标签参数
     * @return
     */
    List<Systems> listByUserId(String param);
}