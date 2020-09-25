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
    //117位长度
    //C#:<RSAKeyValue><Modulus>oDe5HWuS+M7TlKrqB47m0CBMqgaY6WmHqvgwSlwNyTTmbDIxsCxlanwsEINxxwBk2lp8bKeYI5odLUs4iRYEH9ruOU6gNlDmzr5JUYW1q+ekKEzbI5u9sGaEjpVM1t8R4v+j6HB3cv+2zDog3+n5mIRQWXUZXl+rQgKe8Og7HW0=</Modulus><Exponent>AQAB</Exponent><P>1mslE1Xx8GU2zpWxbwa0S9GPSflcDTpIz+4Thci1pIzpZrfXnSyx6Gt+ZDIy50Xlevj9psfoc7cb46+D9qYEyQ==</P><Q>v0nDr0b7bCdBjPA/PLmNt7k90fBapxJIIT7GM4zGlRkQFhWtuDyAa9Zoefw2epyV0u3mKM4TIwPRnWztYesZhQ==</Q><DP>X0JiymBYiHCvFMC0f5r+5uBHRJogJ1BJoPcofTtaV6yy1QvAIvgEYjm/gUw8CTuoZCXSqxINV7t2DIWhnybK4Q==</DP><DQ>q4QtlN0VlS1Bxmiomv9ZhfHv720PK5zl1gFeYFyKtqepV9QKVxbQo2C1fyNZiekbIe00IURdvlivO/OuiczurQ==</DQ><InverseQ>C6bMIu+Z4v7XkdzAdVvA/azKjAGQrQOCYGvwZPsqxOd65FcX8hmSwuIAOXo1Pjl7GeW/P2x0Cis667e5mn7xrg==</InverseQ><D>S6plY26Vh6nuJK0fP5iOi0Zlznq1Jq7vIqB59fRJknfIRtrhuokNEGA6U0YwqsIV30WSs6Hm6RF0rBp1RdC++yjIK7EkMAUu0NNt4ESvcWYHT5jqu/sF4syBMmocBJRRRYPECiQR9BrFZzioZrBY8jPrVGNpjHLHhbFwVxwbNeE=</D></RSAKeyValue>
    //java:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKA3uR1rkvjO05Sq6geO5tAgTKoGmOlph6r4MEpcDck05mwyMbAsZWp8LBCDcccAZNpafGynmCOaHS1LOIkWBB/a7jlOoDZQ5s6+SVGFtavnpChM2yObvbBmhI6VTNbfEeL/o+hwd3L/tsw6IN/p+ZiEUFl1GV5fq0ICnvDoOx1tAgMBAAECgYBLqmVjbpWHqe4krR8/mI6LRmXOerUmru8ioHn19EmSd8hG2uG6iQ0QYDpTRjCqwhXfRZKzoebpEXSsGnVF0L77KMgrsSQwBS7Q023gRK9xZgdPmOq7+wXizIEyahwElFFFg8QKJBH0GsVnOKhmsFjyM+tUY2mMcseFsXBXHBs14QJBANZrJRNV8fBlNs6VsW8GtEvRj0n5XA06SM/uE4XItaSM6Wa3150ssehrfmQyMudF5Xr4/abH6HO3G+Ovg/amBMkCQQC/ScOvRvtsJ0GM8D88uY23uT3R8FqnEkghPsYzjMaVGRAWFa24PIBr1mh5/DZ6nJXS7eYozhMjA9GdbO1h6xmFAkBfQmLKYFiIcK8UwLR/mv7m4EdEmiAnUEmg9yh9O1pXrLLVC8Ai+ARiOb+BTDwJO6hkJdKrEg1Xu3YMhaGfJsrhAkEAq4QtlN0VlS1Bxmiomv9ZhfHv720PK5zl1gFeYFyKtqepV9QKVxbQo2C1fyNZiekbIe00IURdvlivO/OuiczurQJAC6bMIu+Z4v7XkdzAdVvA/azKjAGQrQOCYGvwZPsqxOd65FcX8hmSwuIAOXo1Pjl7GeW/P2x0Cis667e5mn7xrg==
    @Value("${rsaPrivateKey:}")
    private String rsaPrivateKey;
    //java:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgN7kda5L4ztOUquoHjubQIEyqBpjpaYeq+DBKXA3JNOZsMjGwLGVqfCwQg3HHAGTaWnxsp5gjmh0tSziJFgQf2u45TqA2UObOvklRhbWr56QoTNsjm72wZoSOlUzW3xHi/6PocHdy/7bMOiDf6fmYhFBZdRleX6tCAp7w6DsdbQIDAQAB
    // C#:<RSAKeyValue><Modulus>oDe5HWuS+M7TlKrqB47m0CBMqgaY6WmHqvgwSlwNyTTmbDIxsCxlanwsEINxxwBk2lp8bKeYI5odLUs4iRYEH9ruOU6gNlDmzr5JUYW1q+ekKEzbI5u9sGaEjpVM1t8R4v+j6HB3cv+2zDog3+n5mIRQWXUZXl+rQgKe8Og7HW0=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>
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

//    public static void main(String[] args) throws Exception {
//        String privateStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKA3uR1rkvjO05Sq6geO5tAgTKoGmOlph6r4MEpcDck05mwyMbAsZWp8LBCDcccAZNpafGynmCOaHS1LOIkWBB/a7jlOoDZQ5s6+SVGFtavnpChM2yObvbBmhI6VTNbfEeL/o+hwd3L/tsw6IN/p+ZiEUFl1GV5fq0ICnvDoOx1tAgMBAAECgYBLqmVjbpWHqe4krR8/mI6LRmXOerUmru8ioHn19EmSd8hG2uG6iQ0QYDpTRjCqwhXfRZKzoebpEXSsGnVF0L77KMgrsSQwBS7Q023gRK9xZgdPmOq7+wXizIEyahwElFFFg8QKJBH0GsVnOKhmsFjyM+tUY2mMcseFsXBXHBs14QJBANZrJRNV8fBlNs6VsW8GtEvRj0n5XA06SM/uE4XItaSM6Wa3150ssehrfmQyMudF5Xr4/abH6HO3G+Ovg/amBMkCQQC/ScOvRvtsJ0GM8D88uY23uT3R8FqnEkghPsYzjMaVGRAWFa24PIBr1mh5/DZ6nJXS7eYozhMjA9GdbO1h6xmFAkBfQmLKYFiIcK8UwLR/mv7m4EdEmiAnUEmg9yh9O1pXrLLVC8Ai+ARiOb+BTDwJO6hkJdKrEg1Xu3YMhaGfJsrhAkEAq4QtlN0VlS1Bxmiomv9ZhfHv720PK5zl1gFeYFyKtqepV9QKVxbQo2C1fyNZiekbIe00IURdvlivO/OuiczurQJAC6bMIu+Z4v7XkdzAdVvA/azKjAGQrQOCYGvwZPsqxOd65FcX8hmSwuIAOXo1Pjl7GeW/P2x0Cis667e5mn7xrg==";
//        String publicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgN7kda5L4ztOUquoHjubQIEyqBpjpaYeq+DBKXA3JNOZsMjGwLGVqfCwQg3HHAGTaWnxsp5gjmh0tSziJFgQf2u45TqA2UObOvklRhbWr56QoTNsjm72wZoSOlUzW3xHi/6PocHdy/7bMOiDf6fmYhFBZdRleX6tCAp7w6DsdbQIDAQAB";
//        String content = "{userName:\"123456789\", password:\"123456789123456789123456789\"}";
//        String encryptRSA = EncryptUtils.encryptRSA(content, publicStr);
//        System.out.println(encryptRSA);
//        String decryptRSA = EncryptUtils.decryptRSA(encryptRSA, privateStr);
//        System.out.println(decryptRSA);
//    }

}
