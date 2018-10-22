package com.dili.uap.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 */
public class ValidateUtil {

    /**
     * 是否字母、数据或下划线
     * @param content
     * @return
     */
    public static boolean isCharNumUnderline(String content){
        String pattern = "^[0-9a-zA-Z_]{1,}$";
        return Pattern.matches(pattern, content);
    }

}
