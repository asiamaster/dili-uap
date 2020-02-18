package com.dili.uap.component;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.glossary.DataRange;
import com.dili.uap.rpc.ProjectRpc;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目数据范围来源
 */
@Component
public class DataProjectSourceService implements DataAuthSourceService {

    public static final String VALUE_KEY = "value";
    public static final String NAME_KEY = "name";
    @Autowired
    private ProjectRpc projectRpc;

    @Override
    public List listDataAuthes(String param) {
        List<Map<String, Object>> lists = Lists.newArrayList();
        BaseOutput<List<UserDataDto>> selectUserDataTree = projectRpc.selectUserDataTree();
        if(selectUserDataTree.isSuccess()) {
        	List<UserDataDto> data = selectUserDataTree.getData();
        	for (UserDataDto userDataDto : data) {
        		 Map<String, Object> row = new HashMap<>();
                 row.put(VALUE_KEY, userDataDto.getId());
                 row.put(NAME_KEY, userDataDto.getName());
                 lists.add(row);
			}
        }
        return lists;
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
        Map<String, Map> retMap = new HashMap<>();
        BaseOutput<List<UserDataDto>> selectUserDataTree = projectRpc.selectUserDataTree();
        if(selectUserDataTree.isSuccess()) {
        	List<UserDataDto> data = selectUserDataTree.getData();
	        for(String value : values){
	            UserDataDto userDataDto = getUserDataDto(Long.valueOf(value), data);
	            Map<String, Object> valueMap = new HashedMap(2);
	            valueMap.put(VALUE_KEY, userDataDto.getId());
	            valueMap.put(NAME_KEY, userDataDto.getName());
	            retMap.put(value, valueMap);
	        }
        }
        return retMap;
    }
    
    public static UserDataDto getUserDataDto(Long code,List<UserDataDto> list) {
        for (UserDataDto userDataDto : list) {
            if (userDataDto.getId().longValue()==code.longValue()) {
                return userDataDto;
            }
        }
        return null;
    }
}