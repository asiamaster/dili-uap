package com.dili.uap.component;

import com.dili.uap.glossary.DataRange;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据范围来源
 * Created by asiam on 2018/7/9 0009.
 */
@Component
public class DataRangeSourceService implements DataAuthSourceService {

    public static final String VALUE_KEY = "value";
    public static final String NAME_KEY = "name";

    @Override
    public List listDataAuthes(String param) {
        List<Map<String, Object>> lists = Lists.newArrayList();
        for(DataRange dataRange : DataRange.values()){
            Map<String, Object> row = new HashMap<>();
            row.put(VALUE_KEY, dataRange.getCode());
            row.put(NAME_KEY, dataRange.getName());
            lists.add(row);
        }
        return lists;
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        Map<String, Map> retMap = new HashMap<>();
        for(String value : values){
            DataRange dataRange = DataRange.getDataRange(Integer.parseInt(value));
            Map<String, Object> valueMap = new HashedMap(2);
            valueMap.put(VALUE_KEY, dataRange.getCode());
            valueMap.put(NAME_KEY, dataRange.getName());
            retMap.put(value, valueMap);
        }
        return retMap;
    }
}