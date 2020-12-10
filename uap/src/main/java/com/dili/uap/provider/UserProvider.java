package com.dili.uap.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    private UserService userService;

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        UserDto userDto = DTOUtils.newInstance(UserDto.class);
        userDto.setIds(relationIds);
        return userService.list(userDto);
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "userName");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "superiorId" : fkField;
    }
}
