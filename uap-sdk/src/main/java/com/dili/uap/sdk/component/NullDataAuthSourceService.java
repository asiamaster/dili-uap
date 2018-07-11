package com.dili.uap.sdk.component;

import com.dili.uap.sdk.service.DataAuthSourceService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 空数据权限引用来源，只是保证其它项目不报错
 * Created by asiam on 2018/7/11
 */
@Component
public class NullDataAuthSourceService implements DataAuthSourceService {

    @Override
    public List listDataAuthes(String param) {
        return null;
    }
}