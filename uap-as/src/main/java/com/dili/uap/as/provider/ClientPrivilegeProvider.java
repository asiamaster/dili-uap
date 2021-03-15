package com.dili.uap.as.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.as.glossary.ClientPrivilege;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户授权范围提供者
 */
@Component
public class ClientPrivilegeProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER;

    static {
        BUFFER = Stream.of(ClientPrivilege.values())
                .map(e->new ValuePairImpl<Integer>(e.getName(), e.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        ClientPrivilege dispersendTradeFlag = ClientPrivilege.getClientPrivilege((Integer) obj);
        return dispersendTradeFlag == null ? "" : dispersendTradeFlag.getName();
    }
}
