package com.dili.uap.sdk.glossary;

/**
 * 异常类型
 * 用于异常日志表
 * Created by asiam on 2018/5/24.
 */
public enum ExceptionType {

    PARAMS_ERROR("1000","输入参数错误"),
    NOT_AUTH_ERROR("2000", "权限错误"),
    DATA_ERROR("3000", "业务数据错误"),
    APP_ERROR("5000","服务器内部错误");

    private String name;
    private String code ;

    ExceptionType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static ExceptionType getMenuType(Integer code) {
        for (ExceptionType userState : ExceptionType.values()) {
            if (userState.getCode().equals(code)) {
                return userState;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
