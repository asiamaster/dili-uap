package com.dili.uap.sdk.exception;

/**
 * <p>Title:  〈标题〉</p>
 * <p>Description:  〈描述〉</p>
 * <B>Copyright</B> Copyright (c) 2014 www.diligrp.com All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * <p>CreateTime：2014/8/25</p>
 *
 * @author zhukai
 */
public class ManageException extends Exception {
    public ManageException(String msg) {
        super(msg);
    }

    public ManageException(String msg, Exception e) {
        super(msg, e);
    }
}
