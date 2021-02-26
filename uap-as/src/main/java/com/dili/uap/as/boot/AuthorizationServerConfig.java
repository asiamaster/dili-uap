//package com.dili.uap.as.boot;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
///**
// * OAuth的授权服务器配置
// * 注意：必须继承AuthorizationServerConfigurerAdapter类，并使用@Configuration和@EnableAuthorizationServer注解
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserDetailsService userDetailsServiceImpl;
//
//    /**
//     * 授权服务器安全配置：
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) {
//        /**
//         * 获取令牌不需要认证，校验令牌需要认证，允许表单认证
//         */
//        security
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()")
//                .allowFormAuthenticationForClients();
//    }
//
//    /**
//     * 颁发令牌的客户端配置：
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        /**
//         * 在内存中添加四个客户端配置（还可以在数据库中配置）：
//         * client_1只能使用授权码授权方式和刷新令牌的方式获取令牌
//         * client_2只能使用密码授权方式和刷新令牌的方式获取令牌
//         * client_3只能使用简化授权方式获取令牌
//         * client_4只能使用客户端授权方式获取令牌
//         */
//        clients
//                .inMemory()
//                .withClient("client_1")
//                .secret(passwordEncoder.encode("secret_1"))
//                .accessTokenValiditySeconds(3600)
//                .refreshTokenValiditySeconds(604800)
//                .authorizedGrantTypes("authorization_code", "refresh_token")
//                .redirectUris("http://baidu.com")
//                .scopes("all")
//                .and()
//                .withClient("client_2")
//                .secret(passwordEncoder.encode("secret_2"))
//                .accessTokenValiditySeconds(3600)
//                .refreshTokenValiditySeconds(604800)
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("all")
//                .and()
//                .withClient("client_3")
//                .secret(passwordEncoder.encode("secret_3"))
//                .accessTokenValiditySeconds(3600)
//                .authorizedGrantTypes("implicit")
//                .redirectUris("http://baidu.com")
//                .scopes("all")
//                .and()
//                .withClient("client_4")
//                .secret(passwordEncoder.encode("secret_4"))
//                .accessTokenValiditySeconds(3600)
//                .authorizedGrantTypes("client_credentials")
//                .scopes("all");
//    }
//
//    /**
//     * 授权服务器端点配置：
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        /**
//         * 设置用户认证的管理器和生成的令牌的存储方式
//         */
//        endpoints
//                .authenticationManager(this.authenticationManager)
//                .tokenStore(tokenStore());
//
//        /**
//         * 必须设置UserDetailsService才能使用refresh_token：指定使用refresh_token换取access_token时，从哪里获取认证用户信息
//         */
//        endpoints.userDetailsService(userDetailsServiceImpl);
//
//        /**
//         * 设置令牌加强器（生成令牌时使用）
//         */
//        endpoints.tokenEnhancer(jwtAccessTokenConverter());
//    }
//
//    /**
//     * 创建令牌存储对象
//     */
//    @Bean
//    public TokenStore tokenStore(){
//        /**
//         * 使用JwtTokenStore时，必须注入一个JwtAccessTokenConverter，用于解析JWT令牌
//         */
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    /**
//     * 创建JWT令牌转换器（实际上是一种特殊的JWT令牌增强器）
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        /**
//         * 设置JWT令牌的签名key，此外还可以往令牌里添加数据
//         */
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("signingKey");
//        return converter;
//    }
//
//}
