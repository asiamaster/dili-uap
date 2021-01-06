package com.dili.uap.sdk.glossary;

/**
 * 获取UserToken的步骤
 * Created by asiam on 2018/5/21.
 */
public enum TokenStep {
    ACCESS_TOKEN(1,"accessToken有效，直接返回"),
    REFRESH_CACHE(2,"accessToken失效，从refreshToken的临时缓存中获取"),
    REFRESH_TOKEN(3,"accessToken失效，根据refreshToken重新颁发accessToken");

    private String name;
    private Integer code;

    TokenStep(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static TokenStep getTokenStep(Integer code) {
        switch (code) {
            case 1: return TokenStep.ACCESS_TOKEN;
            case 2: return TokenStep.REFRESH_CACHE;
            case 3: return TokenStep.REFRESH_TOKEN;
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
