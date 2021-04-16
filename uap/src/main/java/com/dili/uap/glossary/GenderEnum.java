package com.dili.uap.glossary;

/**
 * <B>Description</B>
 * 性别枚举
 * @author ljf
 * @createTime 2020/12/09
 */
public enum GenderEnum {

    UNKNOWN(-1, "未知"),
    FEMALE(0, "男"),
    MALE(1, "女");

    private Integer code;
    private String name;

    GenderEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GenderEnum getEnabledState(Integer code) {
        for (GenderEnum anEnum : GenderEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        return null != getEnabledState(code) ? getEnabledState(code).getName() : "";
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
