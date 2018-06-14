package com.dili.uap.glossary;

/**
 * 登录类型
 * Created by asiam on 2018/5/21.
 */
public enum LoginType {
    LOGIN(1,"登录"),
    LOGOUT(0,"登出");

    private String name;
    private Integer code ;

    LoginType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LoginType getLoginType(Integer code) {
        for (LoginType userState : LoginType.values()) {
            if (userState.getCode().equals(code)) {
                return userState;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
