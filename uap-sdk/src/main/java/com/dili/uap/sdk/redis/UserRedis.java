package com.dili.uap.sdk.redis;

import com.alibaba.fastjson.JSONObject;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.ManageRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户redis操作
 * Created by Administrator on 2016/10/18.
 */
@Service
public class UserRedis {

    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 根据sessionId获取userId
     * @param sessionId
     * @return
     */
    public Long getSessionUserId(String sessionId) {
        String rst = redisUtil.get(SessionConstants.SESSION_USERID_KEY + sessionId, String.class);
        if(rst == null){
            return null;
        }
        return Long.valueOf(rst);
    }


    /**
     *  根据sessionId获取UserTicket
     * @param sessionId
     * @return
     */
    public UserTicket getUser(String sessionId) {
        return getUser(sessionId, UserTicket.class);
    }

    /**
     * 根据sessionId获取数据，支持转型为指定的clazz<br/>
     * 如果有数据则将redis超时推后SessionConstants.SESSION_TIMEOUT的时间<br/>
     *
     * @param sessionId
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getUser(String sessionId, Class<T> clazz) {
        String sessionData = getSession(sessionId);
        if (StringUtils.isBlank(sessionData)) {
            return null;
        }
        return JSONObject.parseObject(String.valueOf(JSONObject.parseObject(sessionData).get(SessionConstants.LOGGED_USER)), clazz);
    }
    
    /**
     * 根据sessionId获取String数据<br/>
     * 如果有数据则将redis超时推后SessionConstants.SESSION_TIMEOUT的时间
     * @param sessionId
     * @return
     */
    private String getSession(String sessionId){
        String sessionData = redisUtil.get(SessionConstants.SESSION_KEY_PREFIX + sessionId, String.class);
        if (sessionData != null) {
            redisUtil.set(SessionConstants.SESSION_KEY_PREFIX + sessionId, sessionData, SessionConstants.SESSION_TIMEOUT);
        }
        return sessionData;
    }

}
