package com.dili.uap.handler;

import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.constant.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

/**
 * 站内信处理器
 * @author: WM
 * @time: 2021/1/15 14:47
 */
@Component
public class UapWebSocketHandler extends TextWebSocketHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        Object refreshToken = session.getAttributes().get(SessionConstants.REFRESH_TOKEN_KEY);
        Long userId = (Long)session.getAttributes().get(SessionConstants.USER_ID_KEY);
        if (userId != null) {
            // 用户连接成功，放入在线用户缓存
            if(!WsSessionManager.containsKey(userId.toString())) {
                WsSessionManager.add(userId.toString(), session);
            }
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Long userId = (Long)session.getAttributes().get(SessionConstants.USER_ID_KEY);
        log.debug("server 接收到用户[" + userId + "]发送的消息:" + payload);
        session.sendMessage(new TextMessage("server 发送给用户[" + userId + "]消息:" + payload + ",时间:" + LocalDateTime.now().toString()));
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        Object token = session.getAttributes().get(SessionConstants.REFRESH_TOKEN_KEY);
        Long userId = (Long)session.getAttributes().get(SessionConstants.USER_ID_KEY);
        if (userId != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(userId.toString());
        }
    }
}