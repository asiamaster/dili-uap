package com.dili.uap.sdk.domain.dto;

import com.dili.ss.dto.IBaseDomain;

/**
 * 通告消息
 * @author wm
 */
public interface AnnunciateMessage extends IBaseDomain {

    /**
     * 调用API发送消息时是annunciate的id，用于显示消息详情
     * 调用API撤销消息时是annunciate的id，用于处理需要撤销的消息(目前前端暂未处理)
     * @return
     */
    @Override
    Long getId();
    @Override
    void setId(Long id);

    /**
     * 公告项id， 用于标记消息为已读和删除
     * @return
     */
    Long getItemId();
    void setItemId(Long itemId);

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

    /**
     * 事件类型， 1: 发送公告， 2: 撤销公告
     * @return
     */
    Integer getEventType();
    void setEventType(Integer eventType);
}
