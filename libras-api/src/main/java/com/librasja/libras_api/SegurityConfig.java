package com.librasja.libras_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SegurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desativa CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // permite todas as requisições sem login
        return http.build();
    }
}
