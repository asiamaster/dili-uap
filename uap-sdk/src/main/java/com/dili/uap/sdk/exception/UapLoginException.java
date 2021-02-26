package com.dili.uap.sdk.exception;

/**
 * 登录失败异常
 */
public class UapLoginException extends RuntimeException {
    public UapLoginException(){
        super("登录失败");
    }
    public UapLoginException(String msg){
        super(msg);
    }
}
