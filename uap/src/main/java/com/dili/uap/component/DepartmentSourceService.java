package com.dili.uap.component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.dili.uap.service.DepartmentService;
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
    private DepartmentService departmentService;
    @Override
    public List listDataAuthes(String param) {
        return departmentService.list(null);
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        DepartmentDto department = DTOUtils.newInstance(DepartmentDto.class);
        department.setIds(values);
        List<Department> departments = departmentService.listByExample(department);
        Map<String, Map> retMap = new HashMap<>();
        departments.stream().forEach(t -> {
            retMap.put(t.getId().toString(), DTOUtils.go(t));
        });
//        for(String value : values){
//            Map<String, Object> valueMap = new HashedMap();
//            valueMap.putAll(DTOUtils.go(departmentMapper.selectByPrimaryKey(value)));
//            retMap.put(value, valueMap);
//        }
        return retMap;
    }
}