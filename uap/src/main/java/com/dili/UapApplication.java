package com.dili;

import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.spring.annotation.MapperScan;

//import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

/**
 * 由MyBatis Generator工具自动生成
 */
// =====================  Spring Cloud  =====================
//@EnableHystrixDashboard
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix
//@EnableEurekaClient
// =====================  Spring Boot  =====================
// @EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class, VelocityAutoConfiguration.class })
// @ImportResource(locations = "classpath:applicationContext.xml")
//@ServletComponentScan(basePackages = {"com.dili.uap.filter"})
// @EnableEncryptableProperties
//@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:conf/security.properties")
// @ServletComponentScan
// @EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.dili.logger.sdk", "com.dili.uap.rpc" })
@ComponentScan(basePackages = { "com.dili.ss", "com.dili.uap", "com.dili.logger.sdk" })
@RestfulScan({ "com.dili.uap.rpc", "com.dili.uap.sdk.rpc" })
@MapperScan(basePackages = { "com.dili.uap.dao", "com.dili.ss.dao" })
@DTOScan(value = { "com.dili.ss", "com.dili.uap" })
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class UapApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UapApplication.class, args);
	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) {
//		maxMemory=Eden+Survivor+Old Gen
//		maxMemory是拿到的程序最大可以使用的内存，
//		我们知道 ，Survivor有两个，但只有1个会用到，另一个一直闲置。
//		所以这个值maxMemory是去掉一个Survivor空间的值。
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		System.out.println("maxMemory:" + maxMemory + ",totalMemory:" + totalMemory + ",freeMemory:" + freeMemory);
		System.out.println("项目启动完成!");
	}
}
