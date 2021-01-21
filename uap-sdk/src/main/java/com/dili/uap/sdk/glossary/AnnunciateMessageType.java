package com.dili.uap.sdk.glossary;

/**
 * 平台公告类型
 * @author: WM
 * @time: 2021/1/20 10:17
 */
public enum AnnunciateMessageType {
    ANNUNCIATE(1,"平台公告"),
    TODO(2,"待办事宜"),
    BUSINESS(3,"业务消息");

    private String name;
    private Integer code;

    AnnunciateMessageType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static AnnunciateMessageType getAnnunciateMessageType(Integer code) {
        switch (code) {
            case 1: return AnnunciateMessageType.ANNUNCIATE;
            case 2: return AnnunciateMessageType.TODO;
            case 3: return AnnunciateMessageType.BUSINESS;
            default:  return null;
        }
    }

    public static String getName(Integer code) {
        switch (code) {
            case 1: return AnnunciateMessageType.ANNUNCIATE.getName();
            case 2: return AnnunciateMessageType.TODO.getName();
            case 3: return AnnunciateMessageType.BUSINESS.getName();
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
