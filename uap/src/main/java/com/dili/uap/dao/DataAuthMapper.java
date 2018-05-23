package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.DataAuth;
import com.dili.uap.domain.dto.UserDataAuthCondition;

import java.util.List;

public interface DataAuthMapper extends MyMapper<DataAuth> {

    /**
     * 根据用户Id查询所有数据权限
     * @param userId
     * @return
     */
    List<DataAuth> listByUserId(Long userId);

    /**
     * 根据用户id和其它DataAuth条件查询数据权限
     * @param userDataAuthCondition
     * @return
     */
    List<DataAuth> list(UserDataAuthCondition userDataAuthCondition);
}