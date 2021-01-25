package com.dili.uap.sdk.domain.dto;

import java.util.List;

/**
 * 批量通告消息对象
 * 用于向多目标发送消息
 * @author wm
 */
public interface AnnunciateMessages extends AnnunciateMessage {

    /**
     * 目标id列表，必填
     *
     * @return
     */
    List<String> getTargetIds();
    void setTargetIds(List<String> targetIds);

}
