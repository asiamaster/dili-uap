package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.dili.uap.domain.dto.MenuCondition;
import com.dili.uap.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内链提供者
 */
@Component
public class LinkNameProvider extends BatchDisplayTextProviderSupport {

    @Autowired
    private MenuService menuService;

    // 不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
        //设置主DTO和关联DTO需要转义的字段名，这里直接取resource表的name属性
        batchProviderMeta.setEscapeFiled("name");
        //忽略大小写关联
        batchProviderMeta.setIgnoreCaseToRef(true);
        //主DTO与关联DTO的关联(java bean)属性(外键), 这里取resource_link表的resourceId字段
        batchProviderMeta.setFkField("menuId");
        //关联(数据库)表的主键的字段名，默认取id，这里写出来用于示例用法
        batchProviderMeta.setRelationTablePkField("id");
        return batchProviderMeta;
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        List<Long> ids = new ArrayList<>(relationIds.size());
        for(String relationId : relationIds){
            ids.add(Long.parseLong(relationId));
        }
        MenuCondition menuCondition = DTOUtils.newInstance(MenuCondition.class);
        menuCondition.setIds(ids);
        return menuService.listByExample(menuCondition);
    }

}