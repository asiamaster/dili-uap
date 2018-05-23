package com.dili.uap.controller;

import com.dili.uap.constants.SystemConstants;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import java.util.Objects;

public abstract class BaseController {


    /**
     * 获取保存在Session中的用户对象
     *
     * @param <T>
     * @return
     */
    protected <T> T getSessionUser() {
        return (T) SessionContext.getSessionContext().getUserTicket();
    }

    /**
     * 判断是否集团用户
     *
     * @return
     */
    protected boolean isGroupUser() {
        UserTicket userTicket = getSessionUser();
        //TODO 此处需替换成部门编码
        return Objects.equals(userTicket.getDepName(), SystemConstants.GROUP_CODE);
    }

}
