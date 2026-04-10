package com.librasja.libras_api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.librasja.libras_api.feign")
public class FeignConfig {
    // Configurações de Feign podem ser adicionadas aqui se necessário
}
