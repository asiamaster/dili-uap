package com.dili.uap.domain.dto;

import com.dili.uap.domain.DataAuth;

/**
 * 用于根据用户id和DataAuth的条件查询数据权限
 * This file was generated on 2018-05-23 11:29:55.
 */

public interface UserDataAuthCondition extends DataAuth {

    Long getUserId();
    void setUserId(Long userId);
}