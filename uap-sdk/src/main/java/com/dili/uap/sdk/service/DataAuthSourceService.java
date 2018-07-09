package com.dili.uap.sdk.service;

import java.util.List;

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

}
