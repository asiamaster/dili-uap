package com.dili.uap.glossary;

/**
 * 用户状态
 * Created by asiam on 2018/5/21 0021.
 */
public enum UserState {
    DISABLED(0,"已禁用"),
    NORMAL(1,"已启用"),
    LOCKED(2, "已锁定"),
    INACTIVE(3, "未激活");

    private String name;
    private Integer code ;

    UserState(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static UserState getUserState(Integer code) {
        for (UserState userState : UserState.values()) {
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
