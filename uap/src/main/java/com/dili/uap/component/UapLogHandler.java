//package com.dili.uap.component;
//
//import com.alibaba.fastjson.JSON;
//import com.dili.commons.rabbitmq.RabbitMQMessageService;
//import com.dili.logger.sdk.glossary.LoggerConstant;
//import com.dili.ss.dto.IBaseDomain;
//import com.dili.ss.oplog.base.LogHandler;
//import com.dili.ss.oplog.dto.LogContext;
//import com.dili.uap.domain.dto.OperationLog;
//import com.dili.uap.sdk.domain.UserTicket;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.time.LocalDateTime;
//
//@Component
//public class UapLogHandler implements LogHandler {
//
//	@SuppressWarnings("all")
//	@Autowired
//	private RabbitMQMessageService rabbitMQMessageService;
//
//	@Override
//	public void log(String content, Method method, Object[] args, String params, LogContext logContext) {
//		IBaseDomain baseDomain = (IBaseDomain) args[0];
//		System.out.println("content:" + content);
//		OperationLog operationLog = new OperationLog();
//		operationLog.setBusinessType("test");
//		operationLog.setBusinessId(baseDomain.getId());
//		operationLog.setOperationType("edit");
//		operationLog.setContent(content);
//		operationLog.setNotes("UAP操作日志测试Firm修改");
//		UserTicket userTicket = (UserTicket) logContext.getUser();
//		operationLog.setOperatorId(userTicket.getId());
//		operationLog.setMarketId(userTicket.getFirmId());
//		operationLog.setCreateTime(LocalDateTime.now());
//		String json = JSON.toJSONString(operationLog);
//		System.out.println("发送消息:" + json);
//        rabbitMQMessageService.send(LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, json);
//	}
//
//
//}
