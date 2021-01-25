package com.dili.uap.boot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by asiam on 2019/4/8
 */
@Configuration
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class RabbitConfiguration {

    public static final String UAP_TOPIC_EXCHANGE = "diligrp.uap.topicExchange";
    public static final String UAP_ADD_USER_KEY = "uap_addUserKey";
    public static final String UAP_ADD_USER_QUEUE = "uap.addUser.queue";

    public static final String UAP_CHANGE_PASSWORD_KEY = "uap_changePasswordKey";
    public static final String UAP_CHANGE_PASSWORD_QUEUE = "uap.changePassword.queue";

    public static final String UAP_ANNUNCIATE_KEY = "uap_annunciateKey";
    public static final String UAP_ANNUNCIATE_QUEUE = "uap.annunciate.queue";

    @Bean("messageConverter")
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        return new TopicExchange(UAP_TOPIC_EXCHANGE, true, false);
    }

    /**
     * 添加和更新用户时发消息
     * @return
     */
    @Bean("addUserQueue")
    public Queue addUserQueue() {
        return new Queue(UAP_ADD_USER_QUEUE, true, false, false);
    }

    @Bean("addUserBinding")
    public Binding addUserBinding() {
        return BindingBuilder.bind(addUserQueue()).to(topicExchange()).with(UAP_ADD_USER_KEY);
    }

    /**
     * 更新用户密码时发消息
     * @return
     */
    @Bean("changePasswordQueue")
    public Queue changePasswordQueue() {
        return new Queue(UAP_CHANGE_PASSWORD_QUEUE, true, false, false);
    }

    @Bean("changePasswordBinding")
    public Binding changePasswordBinding() {
        return BindingBuilder.bind(changePasswordQueue()).to(topicExchange()).with(UAP_CHANGE_PASSWORD_KEY);
    }

    /**
     * 接收实时平台公告
     * @return
     */
    @Bean("annunciateQueue")
    public Queue annunciateQueue() {
        return new Queue(UAP_ANNUNCIATE_QUEUE, true, false, false);
    }

    @Bean("annunciateBinding")
    public Binding annunciateBinding() {
        return BindingBuilder.bind(annunciateQueue()).to(topicExchange()).with(UAP_ANNUNCIATE_KEY);
    }
}