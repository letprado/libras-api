package com.librasja.libras_api.controller;

import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Controla as sessões de interpretação em Libras. Aqui você pode criar, listar, buscar, iniciar e finalizar sessões.")
@SecurityRequirement(name = "Bearer Authentication")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @PreAuthorize("hasRole('REQUESTER')")
    @Operation(summary = "Criar sessão", description = "Cria uma nova sessão de interpretação em Libras.")
    public ResponseEntity<Session> createSession(@Valid @RequestBody Session session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createSession(session));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('REQUESTER', 'INTERPRETER')")
    @Operation(summary = "Buscar sessão por ID", description = "Busca uma sessão específica pelo ID.")
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        return sessionService.getSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('REQUESTER', 'INTERPRETER')")
    @Operation(summary = "Listar sessões", description = "Lista todas as sessões cadastradas no sistema.")
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Iniciar sessão", description = "Inicia uma sessão e muda o status para CONECTADO.")
    public ResponseEntity<Session> startSession(@PathVariable Long id) {
        return sessionService.startSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Finalizar sessão", description = "Finaliza uma sessão e muda o status para FINALIZADO.")
    public ResponseEntity<Session> finishSession(@PathVariable Long id) {
        return sessionService.finishSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
