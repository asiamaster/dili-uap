package com.dili;

import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@ComponentScan(basePackages = { "com.dili.ss", "com.dili.uap"})
@RestfulScan({"com.dili.uap.rpc", "com.dili.uap.sdk.rpc"})
@MapperScan(basePackages = {"com.dili.uap.dao", "com.dili.ss.uid.dao", "com.dili.ss.dao", "com.dili.ss.quartz.dao"})
@DTOScan(value={"com.dili.ss", "com.dili.uap"}, file="/DTOtargetClass")
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class UapApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UapApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("项目启动完成!");
	}
}
