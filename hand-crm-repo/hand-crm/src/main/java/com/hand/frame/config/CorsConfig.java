package com.hand.frame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域访问
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("*").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "DELETE", "PUT").allowedHeaders("*").maxAge(3600);
	}
}