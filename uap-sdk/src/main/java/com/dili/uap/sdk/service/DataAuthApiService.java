package com.dili.uap.sdk.service;

import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.session.PermissionContext;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 未使用类
 */
public class DataAuthApiService extends AbstractApiService {
    private static final Logger log = LoggerFactory.getLogger(DataAuthApiService.class);

    public DataAuthApiService(String token, String baseUrl) {
        super(token, baseUrl);
    }

    public void refreshAuthData(String type){
        try {
            PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
            Map<String, String> param = new HashMap<>();
            param.put("type", type);
            param.put("userId", SessionContext.getSessionContext().getUserTicket().getId().toString());
            param.put(SessionConstants.ACCESS_TOKEN_KEY, pc.getAccessToken());
            param.put(SessionConstants.REFRESH_TOKEN_KEY, pc.getRefreshToken());
            httpGet("/dataAuthApi/refreshAuthData.api", param);
        } catch (IOException e) {
            log.info("刷新数据权限出现异常!", e);
        }
    }


}
