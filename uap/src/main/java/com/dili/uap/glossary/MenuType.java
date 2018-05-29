package com.dili.uap.glossary;

/**
 * 菜单类型
 * Created by asiam on 2018/5/21.
 */
public enum MenuType {
    DIRECTORY(0,"目录"),
    LINKS(1,"链接"),
    INTERNAL_LINKS(2, "内部链接"),
    RESOURCE(3, "资源"),
    SYSTEM(4, "系统"),
    ;

    private String name;
    private Integer code ;

    MenuType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static MenuType getMenuType(Integer code) {
        for (MenuType userState : MenuType.values()) {
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
