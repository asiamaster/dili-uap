package com.dili.uap.sdk.service.redis;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.exception.ParameterException;
import com.dili.uap.sdk.util.KeyBuilder;
import com.dili.uap.sdk.util.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 用户和系统 redis操作
 * Created by asiamastor on 2018/6/5.
 */
@Service
public class UserSystemRedis {
    private static final Logger log = LoggerFactory.getLogger(UserSystemRedis.class);

    @Resource(name="manageRedisUtil")
    private ManageRedisUtil redisUtil;

    /**
     * 根据用户ID判断访问系统的权限
     * @param userId
     * @param systemCode    系统编码
     * @return
     */
    public boolean checkUserSystemRight(Long userId, Integer systemType, String systemCode) {
        if (userId == null || StringUtils.isBlank(systemCode)) {
            throw new ParameterException("用户id或系统编码不能为空");
        }
        // 去掉http和https前缀, 判断用户权限
        return checkRedisUserSystemByCode(userId, systemType, systemCode);
    }

    /**
     * 获取用户有权限的系统列表
     * @param userId
     * @return
     */
    public List<Systems> getRedisUserSystems(Long userId, Integer systemType){
        String mes = (String)redisUtil.get(KeyBuilder.buildUserSystemKey(userId.toString(), systemType));
        if(StringUtils.isBlank(mes)){
            return new ArrayList<>();
        }
        byte[] after = null;
        //使用BASE64解码
        after = Base64.getDecoder().decode(mes);
        List systems = (List) SerializeUtil.unserialize(after);
        if(systems == null){
            return null;
        }
        //由于反序列化出的对象无法迭代，所以重新放到新的List对象
        List<Systems> systemList = new ArrayList(systems.size());
        for(Object system : systems){
            systemList.add(DTOUtils.as(system, Systems.class));
        }
        return systemList;
    }

    /**
     * 从redis根据用户id判断是否有菜单URL的访问权限
     *
     * @param userId
     * @param systemCode
     * @return
     */
    private boolean checkRedisUserSystemByCode(Long userId, Integer systemType, String systemCode) {
        List<Systems> systems = getRedisUserSystems(userId, systemType);
        for(Systems system : systems){
            if(system.getCode().equals(systemCode)){
                return true;
            }
        }
        return false;
    }

}
