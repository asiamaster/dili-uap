package com.dili.uap.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by asiam on 2019/4/8
 */
@Configuration
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class RabbitConfiguration {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfiguration.class);
    public static final String UAP_TOPIC_EXCHANGE = "diligrp.uap.topicExchange";
    public static final String UAP_ADD_USER_KEY = "diligrp.uap.addUserKey";
    public static final String UAP_CHANGE_PASSWORD_KEY = "diligrp.uap.changePasswordKey";
    public static final String UAP_ADD_USER_QUEUE = "uap.addUser.queue";
    public static final String UAP_CHANGE_PASSWORD_QUEUE = "uap.changePassword.queue";

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(UAP_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue addUserQueue() {
        return new Queue(UAP_ADD_USER_QUEUE, true, false, false);
    }

    @Bean
    public Queue changePasswordQueue() {
        return new Queue(UAP_CHANGE_PASSWORD_QUEUE, true, false, false);
    }

    @Bean
    public Binding addUserBinding() {
        return BindingBuilder.bind(addUserQueue()).to(topicExchange()).with(UAP_ADD_USER_KEY);
    }

    @Bean
    public Binding changePasswordBinding() {
        return BindingBuilder.bind(changePasswordQueue()).to(topicExchange()).with(UAP_CHANGE_PASSWORD_KEY);
    }

    //=========================================================================================================
    //消息交换机
    public static final String LOGGER_TOPIC_EXCHANGE = "dili.logger.topicExchange";
    public static final String LOGGER_ADD_OPERATION_KEY = "dili.logger.addOperationKey";
    public static final String LOGGER_ADD_OPERATION_QUEUE = "dili.logger.addOperationQueue";

    @Bean
    public TopicExchange operationTopicExchange() {
        return new TopicExchange(LOGGER_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue addOperationQueue() {
        return new Queue(LOGGER_ADD_OPERATION_QUEUE, true, false, false);
    }

    @Bean
    public Binding addOperationBinding() {
        return BindingBuilder.bind(addOperationQueue()).to(operationTopicExchange()).with(LOGGER_ADD_OPERATION_KEY);
    }
}