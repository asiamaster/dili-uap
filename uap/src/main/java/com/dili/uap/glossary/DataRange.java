package com.dili.uap.glossary;

/**
 * Created by asiam on 2018/7/9 0009.
 */
public enum DataRange {
    ALL(1,"所有人"),
    SELF(0,"个人");

    private String name;
    private Integer code ;

    DataRange(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static DataRange getDataRange(Integer code) {
        for (DataRange dataRange : DataRange.values()) {
            if (dataRange.getCode().equals(code)) {
                return dataRange;
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