package com.dili.uap.glossary;

/**
 * ws事件类型
 * Created by asiam on 2021/1/25
 */
public enum WsEventType {
    SEND_ANNUNCIATE(1,"发送公告"),
    WITHDRAW_ANNUNCIATE(2,"撤销公告");

    private String name;
    private Integer code ;

    WsEventType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static WsEventType getWsEventType(Integer code) {
        switch (code) {
            case 1: return WsEventType.SEND_ANNUNCIATE;
            case 2: return WsEventType.WITHDRAW_ANNUNCIATE;
            default:  return null;
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}