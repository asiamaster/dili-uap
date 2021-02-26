//package com.dili.uap.as.boot;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.UUID;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
///**
// * 此类在spring boot 2.4.*版本才支持
// * maven依赖:spring-security-oauth2-authorization-server
// */
////@Configuration
////@EnableWebSecurity
////@Import(OAuth2AuthorizationServerConfiguration.class)
//public class AuthServerConfiguration {
//
//
//	// formatter:off
//	@Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		http
//				.authorizeRequests(authorizeRequests ->
//						authorizeRequests.anyRequest().authenticated()
//				)
//				.formLogin(withDefaults());
//		return http.build();
//	}
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails userDetails = User.builder()
//				.username("lengleng")
//				.password("{noop}123456")
//				.authorities("ROLE_USER")
//				.build();
//		return new InMemoryUserDetailsManager(userDetails);
//	}
//
//	@Bean
//	public RegisteredClientRepository registeredClientRepository() {
//		RegisteredClient client = RegisteredClient.withId("pig")
//				.clientId("pig")
//				.clientSecret("pig")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//				.authorizationGrantTypes(authorizationGrantTypes -> {
//					authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//					authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//				})
//				.redirectUri("http://localhost:8080/renren-admin/sys/oauth2-sso")
//				.build();
//		return new InMemoryRegisteredClientRepository(client);
//	}
//
//
//	@Bean
//	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
//		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//		keyPairGenerator.initialize(2048);
//		KeyPair keyPair = keyPairGenerator.generateKeyPair();
//		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//		// @formatter:off
//		RSAKey rsaKey= new RSAKey.Builder(publicKey)
//				.privateKey(privateKey)
//				.keyID(UUID.randomUUID().toString())
//				.build();
//		JWKSet jwkSet = new JWKSet(rsaKey);
//		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//	}
//}
