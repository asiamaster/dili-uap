//package com.dili.uap.as.boot;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import javax.annotation.Resource;
//
///**
// * Spring Security的Web安全配置
// * 注意：必须继承WebSecurityConfigurerAdapter类，并使用@EnableWebSecurity注解
// */
//@EnableWebSecurity
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Resource(name="userDetailsServiceImpl")
//    UserDetailsService userDetailsServiceImpl;
//
//    /**
//     * 用户认证配置
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        /**
//         * 指定用户认证时，默认从哪里获取认证用户信息
//         */
//        auth.userDetailsService(userDetailsServiceImpl);
//    }
//
//    /**
//     * 微服务的Http安全配置
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        /**
//         * 不允许跨域请求，表单认证请求不需要权限，其它请求都需要认证
//         */
//        http
//                .csrf()
//                .disable()
//                .formLogin()
//                .permitAll()
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
//
//
