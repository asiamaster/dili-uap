package com.dili.uapadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class UapAdminApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
            SpringApplication.run(UapAdminApplication.class, args);
    }
}
