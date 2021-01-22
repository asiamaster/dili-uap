package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.boot.RabbitConfiguration;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * web socket接口
 * @author: WM
 * @time: 2021/1/20 9:32
 */
@RestController
@RequestMapping("/api/ws")
public class WebSocketApi {
    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    /**
     * 发送平台公告
     * @param annunciateMessage
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendAnnunciate")
    public BaseOutput sendAnnunciate(AnnunciateMessage annunciateMessage) {
        try {
            WebSocketSession webSocketSession = WsSessionManager.get(annunciateMessage.getTargetId());
            //如果连接在本地，则直接发(多实例，用户的连接可能在其它UAP实例)
            if(webSocketSession != null){
                webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(annunciateMessage)));
                return BaseOutput.success("消息发送成功");
            }
            //如果当前服务器没有连接，则发送广播消息
            rabbitMQMessageService.send(RabbitConfiguration.UAP_TOPIC_EXCHANGE, RabbitConfiguration.UAP_ANNUNCIATE_KEY, JSON.toJSONString(annunciateMessage));
            return BaseOutput.success("消息发送成功");
        } catch (Exception e) {
            return BaseOutput.failure("消息发送失败:"+e.getMessage());
        }
    }
}
