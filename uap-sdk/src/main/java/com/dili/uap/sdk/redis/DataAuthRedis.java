package com.dili.uap.sdk.redis;

import com.alibaba.fastjson.JSONObject;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.KeyBuilder;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据权限redis操作
 * Created by asiamaster on 2017/7/5.
 */
@Service
public class DataAuthRedis {
    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 根据userId和数据权限type获取数据权限详情列表
     * @param refCode data_auth_ref表的code字段
     * @param userId    用户id
     * @return  UserDataAuth列表
     */
    public List<Map> dataAuth(String refCode, Long userId, Integer systemType) {
        BoundSetOperations<String, String> boundSetOperations = redisUtil.getRedisTemplate().boundSetOps (KeyBuilder.buildUserDataAuthKey(userId.toString(), systemType));
        List<Map> dataAuthList = new ArrayList<>();
        if(boundSetOperations.size() <= 0) {
            return dataAuthList;
        }
        //根据类型过滤
        for(String dataAuthJson : boundSetOperations.members()) {
            JSONObject dataAuth = JSONObject.parseObject(dataAuthJson);
            if(dataAuth.get("refCode").equals(refCode)){
//                String value = dataAuth.getString("value");
                dataAuthList.add(dataAuth);
            }
        }
        return dataAuthList;
    }

    /**
     * 根据userId和数据权限type获取数据权限value列表
     * @param refCode data_auth_ref表的code字段
     * @param userId    用户id
     * @return  UserDataAuth List<Map>
     */
    public List<String> dataAuthValues(String refCode, Long userId) {
        BoundSetOperations<String, String> boundSetOperations = redisUtil.getRedisTemplate().boundSetOps (SessionConstants.USER_DATA_AUTH_KEY + userId);
        List<String> dataAuthList = new ArrayList<>();
        if(boundSetOperations.size()<=0) {
            return dataAuthList;
        }

        //根据类型过滤
        for(String dataAuthJson : boundSetOperations.members()) {
            JSONObject dataAuth = JSONObject.parseObject(dataAuthJson);
            if(dataAuth.get("refCode").equals(refCode)){
                dataAuthList.add(dataAuth.getString("value"));
            }
        }
        return dataAuthList;
    }

    /**
     * 指定用户的数据权限UserDataAuth的Map
     * @param userId
     * @return UserDataAuth List<Map>
     */
    public List<Map> dataAuth(Long userId) {
        BoundSetOperations<String, String> boundSetOperations = redisUtil.getRedisTemplate().boundSetOps (SessionConstants.USER_DATA_AUTH_KEY + userId);
        List<Map> dataAuthMap = new ArrayList<>();
        if(boundSetOperations.size() <= 0) {
            return dataAuthMap;
        }
        //根据类型过滤
        for(String userDataAuthJson : boundSetOperations.members()) {
            dataAuthMap.add(JSONObject.parseObject(userDataAuthJson));
        }
        return dataAuthMap;
    }
}
