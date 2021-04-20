package com.dili.uap.sdk.service;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.RSAKeyPair;
import com.dili.ss.util.RSAUtils;
import com.dili.uap.sdk.config.DynamicConfig;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.SystemType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * JWT auth0 服务
 * Using RS256
 */
@Service
public class UserJwtService extends JwtService {

    @Resource
    private DynamicConfig dynamicConfig;

    private static RSAKeyPair oauthRsaKeyPair = null;

    /**
     * 初始化密钥
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        uapRsaKeyPair = RSAUtils.createRSAKeyPair(dynamicConfig.getPublicKey(), dynamicConfig.getPrivateKey());
        oauthRsaKeyPair = RSAUtils.createRSAKeyPair(dynamicConfig.getOauthPublicKey(), dynamicConfig.getOauthPrivateKey());
    }

    /**
     * 获取签发的Web user token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateWebUserTokenByRSA256(UserTicket user) {
        return generateUserTokenByRSA256(user, SystemType.WEB);
    }

    /**
     * 获取签发的App user token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateAppUserTokenByRSA256(UserTicket user) {
        return generateUserTokenByRSA256(user, SystemType.APP);
    }

    /**
     * 指定系统类型(SystemType)获取签发的user token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateUserTokenByRSA256(UserTicket user, SystemType systemType) {
        Algorithm algorithm = Algorithm.RSA256(
                uapRsaKeyPair.getPublicKey(), uapRsaKeyPair.getPrivateKey());
        return createToken(algorithm, ISSUER, systemType.name(), DateUtils.addSeconds(new Date(), dynamicConfig.getAccessTokenTimeout(systemType.getCode()).intValue()), user);
    }

    /**
     * 指定系统类型(SystemType)获取签发的oauth user token，返回给前端
     * @param user
     * @return
     * @throws Exception
     */
    public String generateOAuthUserTokenByRSA256(UserTicket user, SystemType systemType) {
        Algorithm algorithm = Algorithm.RSA256(
                oauthRsaKeyPair.getPublicKey(), oauthRsaKeyPair.getPrivateKey());
        return createToken(algorithm, ISSUER, systemType.name(), DateUtils.addSeconds(new Date(), dynamicConfig.getAccessTokenTimeout(systemType.getCode()).intValue()), user);
    }

    /**
     * 验证并获取UserTicket
     * @param token
     * @return
     * @throws Exception
     */
    public UserTicket getUserTicket(String token) {
        try {
            if(token == null){
                return null;
            }
            return getUserTicket(verifierToken(token));
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    /**
     * 验证并获取OAuth UserTicket
     * @param token
     * @return
     * @throws Exception
     */
    public UserTicket getOAuthUserTicket(String token) {
        try {
            if(token == null){
                return null;
            }
            return getUserTicket(verifierOAuthToken(token));
        } catch (JWTVerificationException e) {
            return null;
        }
    }


    /**
     * 验证oauth token
     * @param token
     * @return
     * @throws Exception
     */
    public DecodedJWT verifierOAuthToken(String token) throws JWTVerificationException{
        //其实按照规定只需要传递 publicKey 来校验即可，这可能是auth0 的缺点
        Algorithm algorithm = Algorithm.RSA256(oauthRsaKeyPair.getPublicKey(), oauthRsaKeyPair.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                //Reusable verifier instance 可复用的验证实例
                .build();
        return verifier.verify(token);
    }

    /**
     * 强制从token取UserTicket
     * 绕过签名验证
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
    protected UserTicket getUserTicket(DecodedJWT jwt) {
        String data = jwt.getClaim(CLAIM_DATA_KEY).asString();
        return JSONObject.parseObject(data, UserTicket.class);
    }

    /**
     * 测试方法
     * @param args
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) {
        UserTicket userTicket = DTOUtils.newInstance(UserTicket.class);
        userTicket.setFirmId(1L);
        userTicket.setFirmCode("group");
        userTicket.setUserName("一二三四");
        userTicket.setCardNumber("12345678");
        userTicket.setGender(1);
        userTicket.setPosition("职员");
        userTicket.setRealName("一二三四");
        userTicket.setSerialNumber("1234567890");
        UserJwtService jwtService = new UserJwtService();
        String webToken = jwtService.generateWebUserTokenByRSA256(userTicket);
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
