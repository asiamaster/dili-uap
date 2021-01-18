package com.dili.uap.filter;

import com.alibaba.fastjson.JSONException;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.session.PermissionContext;
import com.dili.uap.sdk.util.WebContent;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket拦截器
 * @author: WM
 * @time: 2021/1/15 10:24
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false. 通过attributes参数设置WebSocketSession的属性
     * @param request
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest request1 = (ServletServerHttpRequest) request;
            if(null != request.getURI().getQuery()) {
                attributes.putAll(getParamMapByQueryUrl(request.getURI().getQuery()));
            }
            PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
            String refreshToken = pc.getRefreshToken();
            if(refreshToken == null){
                return false;
            }
            attributes.put(SessionConstants.REFRESH_TOKEN_KEY, refreshToken);
        }
        return true;
    }

    /**
     * 完成握手之后执行该方法
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }

    /**
     * 将url参数转为Map
     *
     * @param paramStr
     * @returns {{}}
     */
    private Map<String, String> getParamMapByQueryUrl(String paramStr){
        //String paramStr = "a=a1&b=b1&c=c1";
        String[] params = paramStr.split("&");
        Map<String, String> obj = new HashMap();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                try {
                    obj.put(key,value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
