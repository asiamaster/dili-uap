package com.dili.uap.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.glossary.UserState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <B>Description 用户状态的枚举转换 </B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:32
 */
@Component
public class UserStateProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(UserState.values())
                .map(e -> new ValuePairImpl<String>(e.getName(), e.getCode().toString()))
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
