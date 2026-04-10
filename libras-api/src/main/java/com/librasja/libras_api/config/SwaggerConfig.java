package com.librasja.libras_api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI librasApiOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .name("Bearer Authentication")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info()
                    .title("Libras API")
                    .description("API para gerenciamento de sessões de interpretação em Libras. Projeto acadêmico desenvolvido por estudante para a disciplina Java Advanced. Permite criar sessões, dar feedback e acompanhar o serviço de interpretação. Feito para facilitar a vida de quem precisa de intérprete de Libras.")
                    .version("1.0.0")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório no GitHub")
                        .url("https://github.com/letprado/libras-api"));
    }

    @Bean
    public OpenApiCustomizer customizeOpenApi() {
        return openApi -> openApi.getInfo().setDescription("API para gerenciamento de sessões de interpretação em Libras. Projeto acadêmico feito por estudante. Permite criar sessões, dar feedback e acompanhar o serviço de interpretação.");
    }
}
