package com.dili.uap.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.FirmState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 企业状态提供者
 * 
 * @author wm
 * @createTime 2020/9/11
 */
@Component
public class FirmStateProvider implements ValueProvider {

	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		List<ValuePair<?>> list = new ArrayList<ValuePair<?>>(FirmState.values().length);
		for (FirmState state : FirmState.values()) {
			list.add(new ValuePairImpl<Integer>(state.getName(), state.getValue()));
		}
		return list;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if (obj == null || "".equals(obj)) {
			return null;
		}
		for (FirmState state : FirmState.values()) {
			if (state.getValue().equals(obj)) {
				return state.getName();
			}
		}
		return null;
	}
}
