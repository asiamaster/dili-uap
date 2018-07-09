package com.dili.uap.sdk.component;

import com.dili.uap.sdk.service.DataAuthSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 数据权限引用来源
 * Created by asiam on 2018/7/9 0009.
 */
@Component
public class DataAuthSource {

    @Autowired
    private Map<String, DataAuthSourceService> dataAuthSourceServiceMap;


    public Map<String, DataAuthSourceService> getDataAuthSourceServiceMap() {
        return dataAuthSourceServiceMap;
    }

}