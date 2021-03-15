package com.dili.uap.as;

import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.text.DecimalFormat;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = { "com.dili.uap.sdk.rpc" })
@ComponentScan(basePackages = { "com.dili.ss", "com.dili.uap"})
@RestfulScan({ "com.dili.uap.sdk.rpc"})
@MapperScan(basePackages = { "com.dili.uap.as.mapper", "com.dili.ss.dao" })
@DTOScan(value = { "com.dili.ss", "com.dili.uap"})
public class UapAsApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UapAsApplication.class, args);
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
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        System.out.println("maxMemory:" + decimalFormat.format(maxMemory) + ",totalMemory:" + decimalFormat.format(totalMemory) + ",freeMemory:" + decimalFormat.format(freeMemory));
        System.out.println("项目启动完成!");
    }

    /**
     * 设置服务端口
     */
//    @SuppressWarnings("rawtypes")
//    @Bean
//    public WebServerFactoryCustomizer webServerFactoryCustomizer(){
//        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
//            @Override
//            public void customize(ConfigurableServletWebServerFactory factory) {
//                factory.setPort(8081);
//            }
//        };
//    }
}
