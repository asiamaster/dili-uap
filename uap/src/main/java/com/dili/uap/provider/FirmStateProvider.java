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
 * @author wm
 * @createTime 2020/9/11
 */
@Component
public class FirmStateProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(FirmState.values())
                .map(e -> new ValuePairImpl<String>(e.getName(), e.getValue().toString()))
                .collect(Collectors.toList()));

    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null || "".equals(obj)) {
            return null;
        }
        for (ValuePair<?> valuePair : BUFFER) {
            if (obj.toString().equals(valuePair.getValue())) {
                return valuePair.getText();
            }
        }
        return null;
    }
}
