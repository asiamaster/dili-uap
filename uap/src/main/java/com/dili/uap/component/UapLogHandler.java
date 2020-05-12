package com.dili.uap.component;

import com.alibaba.fastjson.JSON;
import com.dili.logger.sdk.boot.LoggerRabbitProducerConfiguration;
import com.dili.logger.sdk.dto.CorrelationDataExt;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.oplog.base.LogHandler;
import com.dili.ss.oplog.dto.LogContext;
import com.dili.uap.domain.dto.OperationLog;
import com.dili.uap.sdk.domain.UserTicket;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UapLogHandler implements LogHandler {
//    @Autowired
//    private AmqpTemplate amqpTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private LoggerRabbitProducerConfiguration rabbitProducerConfiguration;

	@Override
	public void log(String content, Method method, Object[] args, String params, LogContext logContext) {
		IBaseDomain baseDomain = (IBaseDomain) args[0];
		System.out.println("content:" + content);
		OperationLog operationLog = new OperationLog();
		operationLog.setBusinessType("test");
		operationLog.setBusinessId(baseDomain.getId());
		operationLog.setOperationType("edit");
		operationLog.setContent(content);
		operationLog.setNotes("UAP操作日志测试Firm修改");
		UserTicket userTicket = (UserTicket) logContext.getUser();
		operationLog.setOperatorId(userTicket.getId());
		operationLog.setMarketId(userTicket.getFirmId());
		operationLog.setCreateTime(LocalDateTime.now());
		String json = JSON.toJSONString(operationLog);
		System.out.println("发送消息:" + json);
		sendMsg(LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, json);
	}

	private void sendMsg(String exchange, String routingKey, String json) {
		String uuid = UUID.randomUUID().toString();
		Message message = MessageBuilder.withBody(json.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(uuid).build();
		this.rabbitTemplate.setReturnCallback(rabbitProducerConfiguration);
		this.rabbitTemplate.setConfirmCallback(rabbitProducerConfiguration);

		// 使用继承扩展的CorrelationData 、id消息流水号
		CorrelationDataExt correlationData = new CorrelationDataExt(uuid);
		correlationData.setMessage(message);
		correlationData.setExchange(exchange);
		correlationData.setRoutingKey(routingKey);
		this.rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
	}
}
