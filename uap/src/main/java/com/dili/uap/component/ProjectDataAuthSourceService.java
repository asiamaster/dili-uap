package com.dili.uap.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.domain.Project;
import com.dili.uap.glossary.DataRange;
import com.dili.uap.rpc.ProjectRpc;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.google.common.collect.Lists;

@Component
public class ProjectDataAuthSourceService implements DataAuthSourceService {
	@Autowired
	private ProjectRpc projectRpc;
	
	@Override
    public List listDataAuthes(String param) {
		List<Project> data = projectRpc.selectAll().getData();
        return data;
    }

    @Override
    public Map<String, Map> bindDataAuthes(String param, List<String> values) {
    	Map<String, Map> retMap = new HashMap<>();
        for(String value : values){
            Map<String, Object> valueMap = new HashedMap();
            valueMap.putAll(DTOUtils.go(projectRpc.get(Long.valueOf(value)).getData()));
            retMap.put(value, valueMap);
        }
        return retMap;
    }


}
