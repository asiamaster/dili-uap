package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserDataAuthMapper extends MyMapper<UserDataAuth> {

    /**
     * 根据登录用户id和选择用户id删除，选择用户的数据权限
     * @param param
     */
    void deleteUserDataAuth(Map param);
    /**
     * 根据登录用户id和用户数据Code，查询用户的数据权限值
     * @param userId code
     * @return
     */
	 List<String> selectUserDataAuthValue(@Param("userId")Long userId,@Param("refCode") String refCode);
	 
}