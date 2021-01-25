package com.dili.uap.component;

import com.alibaba.fastjson.JSON;
import com.dili.ss.util.AESUtils;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

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
    public void addUser(Channel channel, Message message) {
        logger.info("收到消息: " + message);
        try {
            String data = new String(message.getBody(), "UTF-8");
            String json = AESUtils.decrypt(data, aesKey);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
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
    public void changePassword(Channel channel, Message message) {
        logger.info("收到消息: " + message);
        try {
            String data = new String(message.getBody(), "UTF-8");
            String json = AESUtils.decrypt(data, aesKey);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info("消息解密: " + json);
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
            handleException(channel, message);
        }
    }

    /**
     * 接收平台公告监听
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_ANNUNCIATE_QUEUE}")
    public void annunciate(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            AnnunciateMessage annunciateMessage = JSON.parseObject(data, AnnunciateMessage.class);
            WebSocketSession webSocketSession = WsSessionManager.get(annunciateMessage.getTargetId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(webSocketSession == null){
                return;
            }
            webSocketSession.sendMessage(new TextMessage(data));
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
            handleException(channel, message);
        }
    }

    /**
     * 处理消息接收后处理异常，扔回MQ
     * @param channel
     * @param message
     */
    private void handleException(Channel channel, Message message){
        // redelivered = true, 表明该消息是重复处理消息
        Boolean redelivered = message.getMessageProperties().getRedelivered();
        try {
            if (redelivered) {
                /**
                 * 1. 对于重复处理的队列消息做补偿机制处理
                 * 2. 从队列中移除该消息，防止队列阻塞
                 */
                // 消息已重复处理失败, 扔掉消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                logger.error("消息 {} 重新处理失败，扔掉消息", message);
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        } catch (IOException ex) {
            logger.error("消息 {} 重放回队列失败 {}", message, ex);
        }
    }
}
