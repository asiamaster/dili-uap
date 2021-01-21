package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * web socket message
 */
public interface WsMessage extends IDTO {

    /**
     * 目标id
     *
     * @return
     */
    String getTargetId();
    void setTargetId(String targetId);

    /**
     * 消息内容
     * @return
     */
    String getMessage();
    void setMessage(String message);

}
