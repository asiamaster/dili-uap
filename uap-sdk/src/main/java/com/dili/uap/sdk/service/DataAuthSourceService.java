package com.dili.uap.sdk.service;

import java.util.List;
import java.util.Map;

/**
 * 数据权限来源接口
 * 所有数据权限引用bean，必须实现该接口
 * Created by asiam on 2018/7/9 0009.
 */
public interface DataAuthSourceService {

    /**
     * 获取数权限据来源
     * 用于在用户管理界面展示所有数据权限
     * @param param data_auth_ref表的param字段内容
     * @return
     */
    List listDataAuthes(String param);

    /**
     * 将数据权限value解析为指定的数据权限详情
     * 用于鉴权接口的获取数据权限详情列表
     * @param param     data_auth_ref表的param字段内容
     * @param values    user_data_auth的value字段列表
     * @return  Map key为数据权限id, 值为数据权限转义后的行数据
     */
    Map<String, Map> bindDataAuthes(String param, List<String> values);

}
