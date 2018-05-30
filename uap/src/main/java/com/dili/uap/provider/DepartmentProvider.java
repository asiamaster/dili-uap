package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.domain.Department;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.dto.DepartmentDto;
import com.dili.uap.domain.dto.FirmQueryDto;
import com.dili.uap.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:32
 */
@Component
public class DepartmentProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    DepartmentService departmentService;

    @Override
    protected List getFkList(List<String> ids, Map map) {
        DepartmentDto departmentDto = DTOUtils.newDTO(DepartmentDto.class);
        departmentDto.setIds(ids);
        return departmentService.list(departmentDto);
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "departmentId" : fkField;
    }
}
