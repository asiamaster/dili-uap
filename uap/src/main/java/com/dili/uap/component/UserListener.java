package com.dili.uap.component;

import com.dili.ss.util.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

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
     * 接收添加用户消息的任务
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_ADD_USER_QUEUE}")
    public void addUserTask(Message message) throws Exception {
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
     * 接收修改密码消息的任务
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitConfiguration.UAP_CHANGE_PASSWORD_QUEUE}")
    public void changePasswordTask(Message message) throws Exception {
        logger.info("收到消息: " + message);
        String data = new String(message.getBody(), "UTF-8");
        String json = AESUtils.decrypt(data, aesKey);
        try {
            logger.info("消息解密: " + json);
        } catch (Exception e) {
            logger.error("转换User对象: {} 出错 {}", message, e);
        }
    }

}
