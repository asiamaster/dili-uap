package com.dili.uap.glossary;

/**
 * 用户授权范围
 */
public enum ClientPrivilege {
    BASIC_INFO(1,"用户基本信息"),
    ORG_INFO(2,"用户组织信息");

    private String name;
    private Integer code ;

    ClientPrivilege(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static ClientPrivilege getClientPrivilege(Integer code) {
        if(code == null){
            return null;
        }
        switch (code){
            case 1 : return ClientPrivilege.BASIC_INFO;
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
