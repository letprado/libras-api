package com.librasja.libras_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI librasApiOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes(
                        "Bearer Authentication",
                        new SecurityScheme()
                                .name("Bearer Authentication")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))
                .info(new Info()
                        .title("Libras API")
                        .version("1.0.0")
                        .description("API para gerenciamento de sessões de interpretação em Libras. Projeto acadêmico desenvolvido por estudante para a disciplina Java Advanced.")
                )
                .servers(List.of(new Server().url("http://localhost:8081").description("Servidor local")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório no GitHub")
                        .url("https://github.com/letprado/libras-api"));
    }

    @Bean
    public OpenApiCustomizer customizeOpenApi() {
        return openApi -> openApi.getInfo().setDescription("API para gerenciamento de sessões de interpretação em Libras. Projeto acadêmico feito por estudante.");
    }
}
