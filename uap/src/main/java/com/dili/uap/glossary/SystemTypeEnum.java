package com.dili.uap.glossary;

/**
 * 系统类型
 * Created by asiam on 2018/5/21.
 */
public enum SystemTypeEnum {
    INTERNAL(1,"内部系统"),
    EXTERNAL(2,"外部系统");

    private String name;
    private Integer code ;

    SystemTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static SystemTypeEnum getMenuType(Integer code) {
        for (SystemTypeEnum userState : SystemTypeEnum.values()) {
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
