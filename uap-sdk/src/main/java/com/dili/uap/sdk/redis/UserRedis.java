package com.dili.uap.sdk.redis;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.manager.SessionRedisManager;
import com.dili.uap.sdk.session.SessionConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户redis操作
 * Created by Administrator on 2016/10/18.
 */
@Service
public class UserRedis {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SessionRedisManager sessionRedisManager;

    /**
     * 根据sessionId获取userId
     * @param sessionId
     * @return
     */
    public Long getSessionUserId(String sessionId) {
        String rst = redisUtil.get(SessionConstants.SESSIONID_USERID_KEY + sessionId, String.class);
        if(rst == null){
            return null;
        }
        return Long.valueOf(rst);
    }

    /**
     * 根据sessionId获取数据，支持转型为指定的clazz<br/>
     * 如果有数据则将redis超时推后SessionConstants.SESSION_TIMEOUT的时间<br/>
     *
     * @param sessionId
     * @return
     */
    public UserTicket getUser(String sessionId) {
        String sessionData = getSession(sessionId);
        if (StringUtils.isBlank(sessionData)) {
            return null;
        }
        //推迟redis session过期时间
        defer(sessionId);
        DTO userDto = JSONObject.parseObject(String.valueOf(JSONObject.parseObject(sessionData).get(SessionConstants.LOGGED_USER)), DTO.class);
        return DTOUtils.proxy(userDto, UserTicket.class);
    }

    /**
     * 推迟redis session过期时间
     * @param sessionId
     */
    private void defer(String sessionId) {
        //推后SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.SESSIONID_USERID_KEY + sessionId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //先根据sessionId找到用户id
        //SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : SessionConstants.SESSION_TIMEOUT
        String userId = getUserIdBySessionId(sessionId);
        //再根据userId，推后sessionId
        //推后SessionConstants.USERID_SESSIONID_KEY + userId : sessionId : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.USERID_SESSIONID_KEY + userId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //推后SessionConstants.USER_SYSTEM_KEY + userId : systems : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.USER_SYSTEM_KEY + userId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //推后SessionConstants.USER_MENU_URL_KEY + userId : menuUrls : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.USER_MENU_URL_KEY + userId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //推后SessionConstants.USER_RESOURCE_CODE_KEY + userId ： resourceCodes : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.USER_RESOURCE_CODE_KEY + userId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        //推后SessionConstants.USER_DATA_AUTH_KEY + userId : userDataAuths : SessionConstants.SESSION_TIMEOUT
        redisUtil.expire(SessionConstants.USER_DATA_AUTH_KEY + userId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
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
            redisUtil.expire(SessionConstants.SESSION_KEY_PREFIX + sessionId, SessionConstants.SESSION_TIMEOUT, TimeUnit.SECONDS);
        }
        return sessionData;
    }

    /**
     * 根据用户id获取sessionId列表
     * @param userId
     * @return
     */
    public List<String> getSessionIdsByUserId(String userId) {
        return sessionRedisManager.getSessionIdsByUserId(userId);
    }

    /**
     * 根据sessionId取用户id
     * @param sessionId
     * @return
     */
    public String getUserIdBySessionId(String sessionId){
        return sessionRedisManager.getUserIdBySessionId(sessionId);
    }

    /**
     * 根据sessionId取用户名
     * @param sessionId
     * @return
     */
    public String getUserNameBySessionId(String sessionId){
        return sessionRedisManager.getUserNameBySessionId(sessionId);
    }

}
