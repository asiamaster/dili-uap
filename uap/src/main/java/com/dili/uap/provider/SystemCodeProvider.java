package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.domain.dto.PositionQueryDto;
import com.dili.uap.sdk.domain.dto.SystemDto;
import com.dili.uap.service.PositionService;
import com.dili.uap.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统提供者
 *
 */
/**
 * <B>Description</B>
 * 系统Provider
 * 根据code转换
 * @author ljf
 * @createTime 2021/04/26
 */
@Component
public class SystemCodeProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    SystemService systemService;

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        SystemDto systemDto = DTOUtils.newInstance(SystemDto.class);
        systemDto.setCodeList(relationIds);
        return systemService.list(systemDto);
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "systemCode" : fkField;
    }

    /**
     * 关联(数据库)表的主键的字段名
     * 默认取id，子类可自行实现
     *
     * @return
     */
    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "code";
    }
}
