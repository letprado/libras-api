package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.JwtAuthResponseDto;
import com.librasja.libras_api.dto.LoginRequestDto;
import com.librasja.libras_api.dto.UserRegistrationDto;
import com.librasja.libras_api.entity.Role;
import com.librasja.libras_api.entity.User;
import com.librasja.libras_api.repository.UserRepository;
import com.librasja.libras_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtAuthResponseDto register(UserRegistrationDto registrationDto) {
        try {
            log.info("=== INICIANDO REGISTRO DE USUÁRIO ===");
            log.info("Username: {}", registrationDto.getUsername());
            log.info("Email: {}", registrationDto.getEmail());
            log.info("Role: {}", registrationDto.getRole());
            
            if (userRepository.existsByUsername(registrationDto.getUsername())) {
                log.warn("Username já existe: {}", registrationDto.getUsername());
                throw new IllegalArgumentException("Username já está em uso");
            }

            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                log.warn("Email já existe: {}", registrationDto.getEmail());
                throw new IllegalArgumentException("Email já está em uso");
            }

            log.info("Criando novo User...");
            User user = User.builder()
                    .username(registrationDto.getUsername())
                    .email(registrationDto.getEmail())
                    .password(passwordEncoder.encode(registrationDto.getPassword()))
                    .role(Role.valueOf(registrationDto.getRole().toUpperCase()))
                    .active(1)
                    .build();

            log.info("Salvando user no banco...");
            userRepository.save(user);
            log.info("User salvo com sucesso! ID: {}", user.getId());

            log.info("Gerando JWT token...");
            String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
            log.info("=== REGISTRO CONCLUÍDO COM SUCESSO ===");

            return new JwtAuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().toString());
        } catch (Exception e) {
            log.error("ERRO AO REGISTRAR USUÁRIO", e);
            throw e;
        }
    }

    public JwtAuthResponseDto login(LoginRequestDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new JwtAuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().toString());
    }
}
