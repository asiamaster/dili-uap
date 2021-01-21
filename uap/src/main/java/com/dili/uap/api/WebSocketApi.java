package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.manager.WsSessionManager;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
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

    /**
     * 发送平台公告
     * @param annunciateMessage
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendAnnunciate")
    public BaseOutput sendAnnunciate(AnnunciateMessage annunciateMessage) throws Exception {
        WebSocketSession webSocketSession = WsSessionManager.get(annunciateMessage.getTargetId());
        if(webSocketSession == null){
            return BaseOutput.failure("目标对象不存在，无法发送");
        }
        webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(annunciateMessage)));
        return BaseOutput.success("消息发送成功");
    }
}
