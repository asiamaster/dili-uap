package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Systems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemMapper extends MyMapper<Systems> {

    /**
     * 根据用户id查询其有权限的系统
     * @param userId
     * @return
     */
    List<Systems> listByUserId(@Param("userId")Long userId, @Param("systemType") Integer systemType);

}