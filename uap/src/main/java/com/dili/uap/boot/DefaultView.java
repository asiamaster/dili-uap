package com.dili.uap.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by asiamaster on 2017/12/13 0013.
 */
@Configuration
public class DefaultView implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/login/index.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
}