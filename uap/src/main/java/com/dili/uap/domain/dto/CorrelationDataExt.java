package com.dili.uap.domain.dto;

import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * CorrelationData的扩展对象
 * 用于在确认发送消息后进行“重试”或者“预警”等操作
 */
public class CorrelationDataExt extends CorrelationData {
    //消息体
    private volatile Object message;
    //交换机
    private String exchange;
    //路由键
    private String routingKey;
    //重试次数
    private int retryCount = 0;

    public CorrelationDataExt(){}

    public CorrelationDataExt(String id){
        super(id);
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
