package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.service.SystemService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统提供者
 */
@Component
public class SystemProvider implements ValueProvider {

	@Autowired
	private SystemService systemService;

	// 不提供下拉数据
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();

		List<Systems> list = systemService.list(null);
		list.forEach(s -> {
			buffer.add(new ValuePairImpl<>(s.getName(), s.getCode()));
		});
		return buffer;
	}

	@Override
	public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
		Long systemId = NumberUtils.toLong(String.valueOf(o),-1L);
		
		Systems system = systemService.list(null).stream()
				.filter(f -> f.getId().equals(systemId))
				.findFirst()
				.orElse(DTOUtils.newInstance(Systems.class));
		return system.getName();
	}

}