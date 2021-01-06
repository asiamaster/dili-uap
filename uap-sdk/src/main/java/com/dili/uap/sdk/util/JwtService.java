package com.dili.uap.sdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.InternalException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.RSAKeyPair;
import com.dili.ss.util.RSAUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.SystemType;
import com.dili.uap.sdk.session.DynaSessionConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * JWT auth0 服务
 * Using RS256
 * @author: WM
 * @time: 2020/12/29 10:00
 */
@Service
public class JwtService {
    /**
     * 发布者
     */
    private static final String ISSUER = "UAP_AUTH0";
    /**
     * 声明数据key
     */
    private static final String CLAIM_DATA_KEY = "data";
    @Resource
    private DynaSessionConstants dynaSessionConstants;

    /**
     * 获取签发的token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateTokenByRSA256(UserTicket user, SystemType systemType) {
        // 获取公钥/私钥
        RSAKeyPair rsa256Key = null;
        try {
            rsa256Key = RSAUtils.getRSAKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(e.getMessage());
        }
        Algorithm algorithm = Algorithm.RSA256(
                rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        return createToken(algorithm, user, systemType);
    }

    /**
     * 获取签发的Web token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateWebTokenByRSA256(UserTicket user) {
        // 获取公钥/私钥
        RSAKeyPair rsa256Key = null;
        try {
            rsa256Key = RSAUtils.getRSAKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(e.getMessage());
        }
        Algorithm algorithm = Algorithm.RSA256(
                rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        return createToken(algorithm, user, SystemType.WEB);
    }

    /**
     * 获取签发的App token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateAppTokenByRSA256(UserTicket user) {
        // 获取公钥/私钥
        RSAKeyPair rsa256Key = null;
        try {
            rsa256Key = RSAUtils.getRSAKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(e.getMessage());
        }
        Algorithm algorithm = Algorithm.RSA256(
                rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        return createToken(algorithm, user, SystemType.APP);
    }

    /**
     * 验证token
     * @param token
     * @return
     * @throws Exception
     */
    public DecodedJWT verifierToken(String token) throws JWTVerificationException{
        // 获取公钥/私钥
        RSAKeyPair rsa256Key = null;
        try {
            rsa256Key = RSAUtils.getRSAKeyPair();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        //其实按照规定只需要传递 publicKey 来校验即可，这可能是auth0 的缺点
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                //Reusable verifier instance 可复用的验证实例
                .build();
        return verifier.verify(token);
    }

    /**
     * 验证并获取UserTicket
     * @param token
     * @return
     * @throws Exception
     */
    public UserTicket getUserTicket(String token) {
        try {
            return getUserTicket(verifierToken(token));
        } catch (JWTVerificationException e) {
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 强制从token取UserTicket
     * @param token
     * @return
     */
    public UserTicket getUserTicketByForce(String token) {
        return getUserTicket(JWT.decode(token));
    }

    /**
     * 获取UserTicket
     * @param jwt
     * @return
     * @throws Exception
     */
    public UserTicket getUserTicket(DecodedJWT jwt) {
        String data = jwt.getClaim(CLAIM_DATA_KEY).asString();
        return JSONObject.parseObject(data, UserTicket.class);
    }

    /**
     * 签发token
     * @param algorithm
     * @param data
     * @param systemType
     * @return
     */
    private String createToken(Algorithm algorithm, Object data, SystemType systemType) {
//        String[] audience  = {"app","web"};
        return JWT.create()
                //发布者
                .withIssuer(ISSUER)
                //观众，相当于接受者
                .withAudience(systemType.name())
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 生成签名的有效期
                .withExpiresAt(DateUtils.addSeconds(new Date(),dynaSessionConstants.getAccessTokenTimeout(systemType.getCode()).intValue()))
                //TODO 测试有效期时间很短
//                .withExpiresAt(DateUtils.addSeconds(new Date(),20))
                //存数据
                .withClaim(CLAIM_DATA_KEY, JSON.toJSONString(data))
                //生效时间
                .withNotBefore(new Date())
                //编号
                .withJWTId(UUID.randomUUID().toString())
                //签入
                .sign(algorithm);
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        UserTicket userTicket = DTOUtils.newInstance(UserTicket.class);
        userTicket.setFirmId(1L);
        userTicket.setFirmCode("group");
        userTicket.setUserName("一二三四");
        userTicket.setCardNumber("12345678");
        userTicket.setGender(1);
        userTicket.setPosition("职员");
        userTicket.setRealName("一二三四");
        userTicket.setSerialNumber("1234567890");
        JwtService jwtService = new JwtService();
        String webToken = jwtService.generateWebTokenByRSA256(userTicket);
        System.out.println("webToken:"+webToken);
        try {
            DecodedJWT jwt = jwtService.verifierToken(webToken);
            System.out.println(jwt.getClaims());
            UserTicket userTicket1 = jwtService.getUserTicket(webToken);
            System.out.println(userTicket1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
