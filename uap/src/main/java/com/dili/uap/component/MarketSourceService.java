package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.sdk.service.DataAuthSourceService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场来源
 * Created by asiam on 2018/7/9 0009.
 */
@Component
public class MarketSourceService implements DataAuthSourceService {

    @Autowired
    private FirmMapper firmMapper;
    @Override
    public List listDataAuthes(String param) {
        return firmMapper.selectAll();
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        Map<String, Map> retMap = new HashMap<>();
        for(String value : values){
            Map<String, Object> valueMap = new HashedMap();
            valueMap.putAll(DTOUtils.go(firmMapper.selectByPrimaryKey(value)));
            retMap.put(value, valueMap);
        }
        return retMap;
    }
}