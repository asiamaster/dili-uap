package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.dili.uap.domain.dto.ResourceDto;
import com.dili.uap.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 市场提供者
 */
@Component
public class ResourceNameProvider extends BatchDisplayTextProviderSupport {

	@Autowired
	private ResourceService resourceService;

	// 不提供下拉数据
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}


	@Override
	protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
		BatchProviderMeta batchProviderMeta = DTOUtils.newDTO(BatchProviderMeta.class);
		//设置主DTO和关联DTO需要转义的字段名，这里直接取resource表的name属性
		batchProviderMeta.setEscapeFiled("name");
		//忽略大小写关联
		batchProviderMeta.setIgnoreCaseToRef(true);
		//主DTO与关联DTO的关联(java bean)属性(外键), 这里取resource_link表的resourceId字段
		batchProviderMeta.setFkField("resourceId");
		//关联(数据库)表的主键的字段名，默认取id，这里写出来用于示例用法
		batchProviderMeta.setRelationTablePkField("id");
		return batchProviderMeta;
	}

	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		ResourceDto resourceDto = DTOUtils.newDTO(ResourceDto.class);
		resourceDto.setIds(relationIds);
		return resourceService.listByExample(resourceDto);
	}



}