package com.dili.uap.component;

import com.dili.uap.dao.FirmMapper;
import com.dili.uap.sdk.service.DataAuthSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 市场来源
 * Created by asiam on 2018/7/9 0009.
 */
@Component
public class MarketSourceService implements DataAuthSourceService {

    @Autowired
    private FirmMapper firmMapper;
    @Override
    public List listDataAuthes(String param) {
        return firmMapper.selectAll();
    }
}