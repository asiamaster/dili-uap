package com.dili.uap.component;

import com.alibaba.fastjson.JSON;
import com.dili.ss.util.AESUtils;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 用户消费组件
 * Created by asiamaster on 2019/4/8
 */
@Component
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class UserListener {
    private static final Logger logger = LoggerFactory.getLogger(UserListener.class);

    @Value("${aesKey:}")
    private String aesKey;

    /**
     * 接收添加用户消息
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_ADD_USER_QUEUE}")
    public void addUser(Message message) throws Exception {
        logger.info("收到消息: " + message);
        String data = new String(message.getBody(), "UTF-8");
        String json = AESUtils.decrypt(data, aesKey);
        try {
            logger.info("消息解密: " + json);
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
        }
    }

    /**
     * 接收修改密码消息
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_CHANGE_PASSWORD_QUEUE}")
    public void changePassword(Message message) throws Exception {
        logger.info("收到消息: " + message);
        String data = new String(message.getBody(), "UTF-8");
        String json = AESUtils.decrypt(data, aesKey);
        try {
            logger.info("消息解密: " + json);
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
        }
    }

    /**
     * 接收平台公告监听
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_ANNUNCIATE_QUEUE}")
    public void annunciate(Message message) throws Exception {
        String data = new String(message.getBody(), "UTF-8");
        try {
            AnnunciateMessage annunciateMessage = JSON.parseObject(data, AnnunciateMessage.class);
            WebSocketSession webSocketSession = WsSessionManager.get(annunciateMessage.getTargetId());
            if(webSocketSession == null){
                return;
            }
            webSocketSession.sendMessage(new TextMessage(data));
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
        }
    }
}
