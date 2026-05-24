package com.librasja.libras_api.controller;

import com.librasja.libras_api.dto.InterpreterRegistrationDto;
import com.librasja.libras_api.dto.JwtAuthResponseDto;
import com.librasja.libras_api.dto.LoginRequestDto;
import com.librasja.libras_api.dto.RequesterRegistrationDto;
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
@Tag(name = "Authentication", description = "Cadastro de surdo (solicitante) e intérprete, e login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/requester")
    @Operation(
            summary = "Cadastrar usuário surdo (solicitante)",
            description = "Cria usuário do tipo REQUESTER (equivalente a SURDO no modelo de dados). Campos: nome, email e senha."
    )
    public ResponseEntity<JwtAuthResponseDto> registerRequester(@Valid @RequestBody RequesterRegistrationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerRequester(dto));
    }

    @PostMapping("/register/interpreter")
    @Operation(
            summary = "Cadastrar intérprete",
            description = "Cria usuário INTERPRETER e perfil profissional (especialidades, descrição, disponibilidade)."
    )
    public ResponseEntity<JwtAuthResponseDto> registerInterpreter(@Valid @RequestBody InterpreterRegistrationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerInterpreter(dto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Fazer login",
            description = "Login único para surdo e intérprete. Use o campo 'nome' cadastrado como username."
    )
    public ResponseEntity<JwtAuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}
