package com.dili.uap.sdk.util;

import com.dili.ss.util.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 鉴权加密工具类
 * @description:
 * @author: WM
 * @time: 2020/9/15 10:03
 */
@Component
public class EncryptUtils {

    @Value("${rsaPrivateKey:}")
    private String rsaPrivateKey;

    @Value("${rsaPublicKey:}")
    private String rsaPublicKey;

    /**
     * 解密
     * @param code
     * @return
     */
    public String decryptRSA(String code) throws Exception {
        return new String(RSAUtils.decryptByPrivateKey(Base64.decodeBase64(code), Base64.decodeBase64(rsaPrivateKey)));
    }

    /**
     * 解密
     * @param code
     * @return
     */
    public static String decryptRSA(String code, String rsaPrivateKey) throws Exception {
        return new String(RSAUtils.decryptByPrivateKey(Base64.decodeBase64(code), Base64.decodeBase64(rsaPrivateKey)));
    }

    /**
     * 加密
     * @param code
     * @return
     */
    public String encryptRSA(String code) throws Exception {
        byte[] publicBytes = Base64.decodeBase64(rsaPublicKey);
        byte[] encryptByPublic = RSAUtils.encryptByPublicKey(code.getBytes(), publicBytes);
        return Base64.encodeBase64String(encryptByPublic);
    }

    /**
     * 加密
     * @param code
     * @return
     */
    public static String encryptRSA(String code, String rsaPublicKey) throws Exception {
        byte[] publicBytes = Base64.decodeBase64(rsaPublicKey);
        byte[] encryptByPublic = RSAUtils.encryptByPublicKey(code.getBytes(), publicBytes);
        return Base64.encodeBase64String(encryptByPublic);
    }

//    public static void main1(String[] args) throws Exception {
//        EncryptUtils encryptUtils = new EncryptUtils();
//        String content = "{userName:\"jt_test\", password:\"asdf1234\"}";
//        String encryptRSA = encryptUtils.encryptRSA(content);
//        System.out.println(encryptRSA);
//        String decryptRSA = encryptUtils.decryptRSA(encryptRSA);
//        System.out.println(decryptRSA);
//    }
}
