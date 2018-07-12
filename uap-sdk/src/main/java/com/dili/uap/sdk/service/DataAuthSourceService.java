package com.dili.uap.sdk.service;

import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.List;
import java.util.Map;

/**
 * 数据权限来源接口
 * 所有数据权限引用bean，必须实现该接口
 * Created by asiam on 2018/7/9 0009.
 */
public interface DataAuthSourceService {

    /**
     * 获取数据来源
     * @param param data_auth_ref表的param内容
     * @return
     */
    List listDataAuthes(String param);

    /**
     *  values 解析为指定的数据权限来源
     * @param param     data_auth_ref表的param内容
     * @param values    user_data_auth的value列表
     * @return  Map key为value, 值为转义后的行数据
     */
    Map<String, Map> bindDataAuthes(String param, List<String> values);

}
