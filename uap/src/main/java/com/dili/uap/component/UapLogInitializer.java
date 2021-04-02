//package com.dili.uap.component;
//
//import com.dili.ss.oplog.base.LogInitializer;
//import com.dili.ss.oplog.dto.LogContext;
//import com.dili.uap.sdk.domain.UserTicket;
//import com.dili.uap.sdk.session.SessionContext;
//import com.dili.uap.sdk.util.WebContent;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
///**
// * 日志上下文初始化器
// */
//@Component
//public class UapLogInitializer implements LogInitializer {
//
//
//    @Override
//    public LogContext init(Method method, Object[] args) {
//        HttpServletRequest request = WebContent.getRequest();
//        LogContext logContext = new LogContext();
//        logContext.setRemoteHost(request.getRemoteHost());
//        logContext.setRequestURI(request.getRequestURI());
//        logContext.setRequestURL(request.getRequestURL().toString());
//        UserTicket user = SessionContext.getSessionContext().getUserTicket();
//        logContext.setUser(user);
//        logContext.setServerPort(request.getServerPort());
//        logContext.setParameterMap(request.getParameterMap());
////        logContext.setBizInfo(...);
//        return logContext;
//    }
//}
