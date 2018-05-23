package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.DataAuth;

import java.util.List;

public interface DataAuthMapper extends MyMapper<DataAuth> {

    List<DataAuth> findByUserId(Long userId);
}