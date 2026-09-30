package com.colegio.rabbit_admin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Rabbit Admin API",
                version = "1.0.0",
                description = "API para gestionar colas, exchanges y bindings de RabbitMQ desde el microservicio rabbit-admin."
        )
)
public class OpenApiConfig {
}
