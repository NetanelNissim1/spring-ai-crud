package com.example.aicrud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot & Spring AI CRUD Catalog API")
                        .version("1.0.0")
                        .description("REST API demonstrating full CRUD operations integrated with Spring AI for intelligent product enrichment and autonomous tool calling.")
                        .contact(new Contact()
                                .name("Spring AI CRUD Team")
                                .url("https://github.com/spring-projects/spring-ai"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://spring.io")));
    }
}
