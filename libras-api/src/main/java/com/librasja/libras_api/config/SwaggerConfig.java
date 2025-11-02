package com.librasja.libras_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Libras API")
                .version("2.0.0")
                .description("""
                    API REST para gerenciamento de sessões de interpretação em Libras.
                    
                    ## Funcionalidades:
                    - Criar e gerenciar sessões de interpretação
                    - Receber feedbacks dos usuários
                    - Suporte a HATEOAS nível 3 (Richardson Maturity Model)
                    
                    ## Público-Alvo:
                    - Pessoas surdas que necessitam de serviços de interpretação
                    - Intérpretes de Libras profissionais
                    
                    ## Tecnologias:
                    - Spring Boot 3.3.4
                    - Java 17
                    - Oracle Database
                    - Spring HATEOAS
                    """
                )
                .contact(new Contact()
                    .name("Equipe Libras API")
                    .email("contato@librasapi.com"))
                .license(new License()
                    .name("Academic License")
                    .url("https://github.com/letprado/libras-api"))
            )
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento"),
                new Server().url("https://api.libras.com").description("Servidor de Produção")
            ));
    }
}

