package com.dili.uap.provier;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.domain.Firm;
import com.dili.uap.service.FirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户提供者
 */
@Component
public class FirmProvider implements ValueProvider {

    @Autowired
    private FirmService firmService;
    //不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();

        List<Firm> list = firmService.list(null);
        list.forEach(firm -> {
            buffer.add(new ValuePairImpl<>(firm.getName(), firm.getCode()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
        return null;
    }

}