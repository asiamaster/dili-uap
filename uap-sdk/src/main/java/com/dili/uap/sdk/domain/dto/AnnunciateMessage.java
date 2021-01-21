package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IBaseDomain;

/**
 * 通告消息
 */
public interface AnnunciateMessage extends IBaseDomain {

    /**
     * 目标id
     *
     * @return
     */
    String getTargetId();
    void setTargetId(String targetId);

    /**
     * 标题
     * @return
     */
    String getTitle();
    void setTitle(String title);

    /**
     * 消息富文本内容
     * @return
     */
    String getContent();
    void setContent(String content);

    /**
     * 消息类型, 1: 平台公告, 2: 待办事宜, 3:业务消息
     *
     * @return
     */
    Integer getType();
    void setType(Integer type);

    /**
     * 未读消息数
     * @return
     */
    Integer getUnreadCount();
    void setUnreadCount(Integer unreadCount);

    /**
     * 消息显示时间(毫秒)
     * @return
     */
    Integer getTimeout();
    void setTimeout(Integer timeout);
}
