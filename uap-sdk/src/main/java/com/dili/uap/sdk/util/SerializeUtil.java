package com.dili.uap.sdk.util;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.System;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * 对象序列化工具
 * @author asiam on 2018/6/5 0005.
 */
public class SerializeUtil {

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unserialize( byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        System system = DTOUtils.newDTO(System.class);
//        system.setId(1L);
//        system.setName("系统");
//        byte[] b= serialize(system);
//        String s = byteArrayToHexStr(b);
//        byte[] c = hexStrToByteArray(s);
//        String s2 = byteArrayToHexStr(c);
//        java.lang.System.out.println(s.equals(s2));
//        java.lang.System.out.println(b.equals(c));
////        java.lang.System.out.println(((System)unserialize(getByteArray(s))).getName());
//    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStrToByteArray(String str)
    {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }
}