package com.dili.uap.as.mapper;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.UserPushInfo;
import org.apache.ibatis.annotations.Param;

public interface UserPushInfoMapper extends MyMapper<UserPushInfo> {

    /**
     * 根据用户id或pushId删除
     *
     * @param userId 用户ID
     * @param pushId
     * @return 受影响行数
     */
    int deleteByUserIdOrPushId(@Param("userId")Long userId, @Param("pushId")String pushId);
}