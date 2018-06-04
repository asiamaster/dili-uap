package com.dili.uap.sdk.exception;

/**
 * 参数异常
 * Created by Administrator on 2016/10/19.
 */
public class ParameterException extends RuntimeException {
    public ParameterException(){
        super("参数异常");
    }
    public ParameterException(String msg){
        super(msg);
    }
}
