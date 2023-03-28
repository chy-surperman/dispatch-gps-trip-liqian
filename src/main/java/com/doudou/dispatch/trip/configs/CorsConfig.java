package com.doudou.dispatch.trip.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {
 
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedOrigins("http://localhost:8080")
//				.allowedMethods("POST", "GET", "OPTIONS", "DELETE","PUT")
//				.allowCredentials(true)
//				.maxAge(3600)
//				.allowedHeaders("x-requested-with,Authorization,Content-Type");
//	}
	
	@Bean  
    public CorsFilter corsFilter() {  
        
        CorsConfiguration corsConfiguration = new CorsConfiguration();  
        corsConfiguration.addAllowedOrigin("*");
        
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("authorization");
        allowedHeaders.add("content-type");
        corsConfiguration.setAllowedHeaders(allowedHeaders);
        
        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("POST");
        allowedMethods.add("GET");
        allowedMethods.add("OPTIONS");
        allowedMethods.add("DELETE");
        corsConfiguration.setAllowedMethods(allowedMethods);
        
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600l);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
        source.registerCorsConfiguration("/**", corsConfiguration); // 4  
        
        return new CorsFilter(source);  
    }  
}
