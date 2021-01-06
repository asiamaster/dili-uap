package com.dili.uap.sdk.glossary;

/**
 * 系统类型
 * Created by asiam on 2018/5/21.
 */
public enum SystemType {
    WEB(1,"WEB"),
    APP(2,"APP");

    private String name;
    private Integer code;

    SystemType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static SystemType getSystemType(Integer code) {
        switch (code) {
            case 1: return SystemType.WEB;
            case 2: return SystemType.APP;
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
