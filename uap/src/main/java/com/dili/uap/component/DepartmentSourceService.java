package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.sdk.service.DataAuthSourceService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门来源
 * Created by asiam on 2018/7/9 0009.
 */
@Component
public class DepartmentSourceService implements DataAuthSourceService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public List listDataAuthes(String param) {
        return departmentMapper.selectAll();
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        Map<String, Map> retMap = new HashMap<>();
        for(String value : values){
            Map<String, Object> valueMap = new HashedMap();
            valueMap.putAll(DTOUtils.go(departmentMapper.selectByPrimaryKey(value)));
            retMap.put(value, valueMap);
        }
        return retMap;
    }
}