package com.app.device_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Device Management API")
                .version("v1")
                .description("API for managing devices")
                .contact(new Contact().name("Dev Team").email("dev@example.com")))
        .servers(
            java.util.List.of(
                new Server().url("http://localhost:8080").description("Local server")))
        .tags(
            java.util.List.of(
                new Tag().name("Device API").description("CRUD operations for devices")));
  }
}
