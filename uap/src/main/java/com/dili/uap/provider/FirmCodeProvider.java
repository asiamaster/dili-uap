package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.dto.FirmQueryDto;
import com.dili.uap.service.FirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场提供者
 * 根据code转换
 */
@Component
public class FirmCodeProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private FirmService firmService;

	// 不提供下拉数据
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
		List<Firm> list = firmService.list(null);
		list.forEach(firm -> {
			buffer.add(new ValuePairImpl<>(firm.getName(), firm.getCode()));
		});
		buffer.add(0, new ValuePairImpl<String>(ValueProvider.EMPTY_ITEM_TEXT, ""));
		return buffer;
	}


	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		FirmQueryDto firmQueryDto = DTOUtils.newDTO(FirmQueryDto.class);
		firmQueryDto.setCodes(relationIds);
		return firmService.list(firmQueryDto);
	}

	/**
	 * 返回主DTO和关联DTO需要转义的字段名
	 * Map中key为主DTO在页面(datagrid)渲染时需要的字段名， value为关联DTO中对应的字段名
	 * @return
	 */
	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
			return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
		}else {
			Map<String, String> map = new HashMap<>();
			//默认显示客户名
			map.put(metaMap.get(FIELD_KEY).toString(), "name");
			return map;
		}
	}

	/**
	 * 关联(数据库)表的主键的字段名
	 * 默认取id，子类可自行实现
	 * @return
	 */
	@Override
	protected String getRelationTablePkField(Map metaMap) {
		return "code";
	}

	/**
	 * 主DTO与关联DTO的关联(java bean)属性(外键)
	 * 先从field属性取，没取到再取_fkField属性
	 * 子类可自行实现
	 * @return
	 */
	@Override
	protected String getFkField(Map metaMap) {
//		String field = (String)metaMap.get(FIELD_KEY);
		String fkField = (String)metaMap.get(FK_FILED_KEY);
		return fkField == null ? "firmCode" : fkField;
	}

}