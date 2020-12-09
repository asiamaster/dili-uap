package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.domain.dto.PositionQueryDto;
import com.dili.uap.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位提供者
 * 根据id转换
 */
@Component
public class PositionProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    PositionService positionService;

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        PositionQueryDto positionQueryDto = DTOUtils.newInstance(PositionQueryDto.class);
        positionQueryDto.setIds(relationIds);
        return positionService.list(positionQueryDto);
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "positionName");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "positionId" : fkField;
    }
}
