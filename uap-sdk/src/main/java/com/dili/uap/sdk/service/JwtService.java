package com.dili.uap.sdk.service;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dili.ss.util.RSAKeyPair;
import com.dili.ss.util.RSAUtils;
import com.dili.uap.sdk.config.DynamicConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    protected static final String ISSUER = "UAP_AUTH0";
    /**
     * 声明数据key
     */
    protected static final String CLAIM_DATA_KEY = "data";

    protected static RSAKeyPair uapRsaKeyPair = null;

    @Resource
    private DynamicConfig dynamicConfig;

    /**
     * 初始化密钥
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        uapRsaKeyPair = RSAUtils.createRSAKeyPair(dynamicConfig.getPublicKey(), dynamicConfig.getPrivateKey());
    }
    /**
     * RSA256算法签发token
     * @param audience  观众，相当于接受者
     * @param issuer    发布者
     * @param expiresAt 签名的有效期
     * @param data      JSON数据
     * @return
     */
    public String generateTokenByRSA256(String issuer, String audience, Date expiresAt, Object data){
        Algorithm algorithm = Algorithm.RSA256(
                uapRsaKeyPair.getPublicKey(), uapRsaKeyPair.getPrivateKey());
        return createToken(algorithm, issuer, audience, expiresAt, data);
    }

    /**
     * 验证token
     * @param token
     * @return
     * @throws Exception
     */
    public DecodedJWT verifierToken(String token) throws JWTVerificationException{
        //其实按照规定只需要传递 publicKey 来校验即可，这可能是auth0 的缺点
        Algorithm algorithm = Algorithm.RSA256(uapRsaKeyPair.getPublicKey(), uapRsaKeyPair.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                //Reusable verifier instance 可复用的验证实例
                .build();
        return verifier.verify(token);
    }

    /**
     * 签发token
     * @param algorithm 算法
     * @param audience  观众，相当于接受者
     * @param issuer    发布者
     * @param expiresAt 签名的有效期
     * @param data      JSON数据
     * @return
     */
    protected String createToken(Algorithm algorithm, String issuer, String audience, Date expiresAt, Object data) {
//        String[] audience  = {"app","web"};
        return JWT.create()
                // 发布者
                .withIssuer(issuer)
                // 观众，相当于接受者
                .withAudience(audience)
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 生成签名的有效期
                .withExpiresAt(expiresAt)
                //存数据
                .withClaim(CLAIM_DATA_KEY, JSON.toJSONString(data))
                //生效时间
                .withNotBefore(new Date())
                //编号
                .withJWTId(UUID.randomUUID().toString())
                //签入
                .sign(algorithm);
    }

}