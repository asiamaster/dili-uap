package com.dili.uap.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.glossary.AnnunciateMessageType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 公告类型提供者
 * 
 * @author wm
 * @createTime 2020/9/11
 */
@Component
public class AnnunciateMessageTypeProvider implements ValueProvider {

	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if (obj == null || "".equals(obj)) {
			return null;
		}
		for (AnnunciateMessageType annunciateMessageType : AnnunciateMessageType.values()) {
			if (annunciateMessageType.getCode().equals(obj)) {
				return annunciateMessageType.getName();
			}
		}
		return null;
	}
}
