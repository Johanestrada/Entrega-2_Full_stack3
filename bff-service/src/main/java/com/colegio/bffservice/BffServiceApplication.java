package com.colegio.bffservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BffServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffServiceApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(@Value("${CORS_ALLOWED_ORIGINS:http://localhost:4173}") String allowedOrigin) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				var mapping = registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
				if ("*".equals(allowedOrigin)) {
					mapping.allowedOriginPatterns("*").allowCredentials(false);
				} else {
					mapping.allowedOrigins(allowedOrigin).allowCredentials(true);
				}
			}
		};
	}

}
