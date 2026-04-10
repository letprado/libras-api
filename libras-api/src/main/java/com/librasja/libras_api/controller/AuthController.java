package com.librasja.libras_api.controller;

import com.librasja.libras_api.dto.JwtAuthResponseDto;
import com.librasja.libras_api.dto.LoginRequestDto;
import com.librasja.libras_api.dto.UserRegistrationDto;
import com.librasja.libras_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário com role INTERPRETER ou REQUESTER")
    public ResponseEntity<JwtAuthResponseDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        JwtAuthResponseDto response = authService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica usuário e retorna JWT token")
    public ResponseEntity<JwtAuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        JwtAuthResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
