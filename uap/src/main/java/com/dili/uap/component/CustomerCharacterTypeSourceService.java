package com.dili.uap.component;

import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户角色身份来源
 */
@Component
public class CustomerCharacterTypeSourceService implements DataAuthSourceService {

    public static final String VALUE_KEY = "value";
    public static final String NAME_KEY = "name";

    @Override
    public List listDataAuthes(String param) {
        List<Map<String, Object>> lists = Lists.newArrayList();
        for(CustomerEnum.CharacterType characterType : CustomerEnum.CharacterType.values()){
            Map<String, Object> row = new HashMap<>();
            row.put(VALUE_KEY, characterType.getValue());
            row.put(NAME_KEY, characterType.getCode());
            lists.add(row);
        }
        return lists;
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        Map<String, Map> retMap = new HashMap<>();
        for(String value : values){
            CustomerEnum.CharacterType characterType = CustomerEnum.CharacterType.getInstance(value);
            Map<String, Object> valueMap = new HashedMap(4);
            valueMap.put(VALUE_KEY, characterType.getCode());
            valueMap.put(NAME_KEY, characterType.getValue());
            retMap.put(value, valueMap);
        }
        return retMap;
    }
}