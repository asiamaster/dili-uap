package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.boot.RabbitConfiguration;
import com.dili.uap.glossary.WsEventType;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import com.dili.uap.sdk.domain.dto.AnnunciateMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

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
        //设置事件类型为发送公告
        annunciateMessage.setEventType(WsEventType.SEND_ANNUNCIATE.getCode());
        return sendMessage(annunciateMessage);
    }

    /**
     * 发送平台公告给多目标id
     * @param annunciateMessages
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendAnnunciates")
    public BaseOutput sendAnnunciates(AnnunciateMessages annunciateMessages) {
        //设置事件类型为发送公告
        annunciateMessages.setEventType(WsEventType.SEND_ANNUNCIATE.getCode());
        return sendMessages(annunciateMessages);
    }

    /**
     * 撤回平台公告
     * @param annunciateMessage， targetId和id必填
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/withdrawAnnunciate")
    public BaseOutput withdrawAnnunciate(AnnunciateMessage annunciateMessage) {
        //设置事件类型为撤销公告
        annunciateMessage.setEventType(WsEventType.WITHDRAW_ANNUNCIATE.getCode());
        return sendMessage(annunciateMessage);
    }

    /**
     * 批量撤销平台公告
     * @param annunciateMessages targetIds和id必填
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/withdrawAnnunciates")
    public BaseOutput withdrawAnnunciates(AnnunciateMessages annunciateMessages) {
        //设置事件类型为发送公告
        annunciateMessages.setEventType(WsEventType.WITHDRAW_ANNUNCIATE.getCode());
        return sendMessages(annunciateMessages);
    }

    /**
     * 发送消息给一个目标， 发送和撤销公告共用该方法
     * @param annunciateMessage
     * @return
     */
    private BaseOutput sendMessage(AnnunciateMessage annunciateMessage){
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

    /**
     * 发送消息给多个目标， 发送和撤销公告共用该方法
     * @param annunciateMessages
     * @return
     */
    public BaseOutput sendMessages(AnnunciateMessages annunciateMessages) {
        try {
            //保存不在本地的目标，发给MQ
            List<String> targetIdsLeft = new ArrayList<>();
            //原始目标列表
            List<String> targetIdsOri = annunciateMessages.getTargetIds();
            //清空目标列表
            annunciateMessages.setTargetIds(null);
            for (String targetId : targetIdsOri) {
                WebSocketSession webSocketSession = WsSessionManager.get(targetId);
                //如果连接在本地，则直接发(多实例，用户的连接可能在其它UAP实例)
                if(webSocketSession != null){
                    webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(annunciateMessages)));
                }else{
                    targetIdsLeft.add(targetId);
                }
            }
            if (targetIdsLeft != null) {
                for (String targetId : targetIdsLeft) {
                    annunciateMessages.setTargetId(targetId);
                    //如果连接不在当前服务器，则发送广播消息
                    rabbitMQMessageService.send(RabbitConfiguration.UAP_TOPIC_EXCHANGE, RabbitConfiguration.UAP_ANNUNCIATE_KEY, JSON.toJSONString(annunciateMessages));
                }
            }
            return BaseOutput.success("消息发送成功");
        } catch (Exception e) {
            return BaseOutput.failure("消息发送失败:"+e.getMessage());
        }
    }
}
