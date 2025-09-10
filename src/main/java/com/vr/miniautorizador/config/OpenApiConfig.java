package com.vr.miniautorizador.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        tags = {
                @Tag(name = "cartoes", description = "Endpoints para criar e consultar saldo"),
                @Tag(name = "transacoes", description = "Endpoint para transação com cartoes"),
        }
)
@Configuration
@SecurityScheme(
        name = "Basic Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenApiConfig {
        @Bean
        public GroupedOpenApi groupedOpenApi() {
                return GroupedOpenApi.builder()
                        .group("miniautorizador")
                        .pathsToMatch("/**")
                        .build();
        }

        @Bean
        public OpenAPI openApi() {
                return new OpenAPI()
                        .info(new Info()
                                .title("MiniAutorizador API")
                                .version("1.0.0")
                                .contact(new Contact().name("Marcelo").email("marcelom.info@gmail.com")));
        }
}