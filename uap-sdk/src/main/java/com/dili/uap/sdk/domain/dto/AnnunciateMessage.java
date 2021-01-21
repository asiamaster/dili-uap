package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IBaseDomain;

/**
 * 通告消息
 */
public interface AnnunciateMessage extends IBaseDomain {

    /**
     * 目标id，必填
     *
     * @return
     */
    String getTargetId();
    void setTargetId(String targetId);

    /**
     * 标题，必填
     * @return
     */
    String getTitle();
    void setTitle(String title);

    /**
     * 消息富文本内容，非必填
     * @return
     */
    String getContent();
    void setContent(String content);

    /**
     * 消息类型, 1: 平台公告, 2: 待办事宜, 3:业务消息，必填
     *
     * @return
     */
    Integer getType();
    void setType(Integer type);

    /**
     * 未读消息数，必填
     * @return
     */
    Integer getUnreadCount();
    void setUnreadCount(Integer unreadCount);

    /**
     * 消息显示时间(毫秒)，默认3秒，非必填
     * @return
     */
    Integer getTimeout();
    void setTimeout(Integer timeout);
}
