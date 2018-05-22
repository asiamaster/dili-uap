package com.dili.uap.sdk.exception;

/**
 * 未登录异常
 * Created by Administrator on 2016/10/19.
 */
public class NotLoginException extends RuntimeException {
    public NotLoginException(){
        super("用户未登录");
    }
    public NotLoginException(String msg){
        super(msg);
    }
}
