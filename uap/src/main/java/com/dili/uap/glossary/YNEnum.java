package com.dili.uap.glossary;

/**
 * YN类型
 * Created by asiam on 2018/5/21.
 */
public enum YNEnum {
    YES(1,"是"),
    NO(0,"否");

    private String name;
    private Integer code ;

    YNEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static YNEnum getMenuType(Integer code) {
        for (YNEnum userState : YNEnum.values()) {
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
