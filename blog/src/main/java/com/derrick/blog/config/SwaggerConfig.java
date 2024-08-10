package com.derrick.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI blogOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Blog API")
                        .description("API for managing blog posts and comments")
                        .version("1.0"));
    }
}
