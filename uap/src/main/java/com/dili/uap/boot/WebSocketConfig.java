//package com.dili.uap.boot;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//
///**
// * WebSocket相关配置
// * @author: WM
// * @time: 2021/1/14 17:05
// */
////@Configuration
////@EnableWebSocket
//public class WebSocketConfig {
//
//    /**
//     * 注入ServerEndpointExporter，这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint。
//     * 要注意，如果使用独立的servlet容器，而不是直接使用springboot的内置容器，就不要注入ServerEndpointExporter，因为它将由容器自己提供和管理。
//     * @return
//     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
//}
