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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Controla as sessões de interpretação em Libras. Aqui você pode criar, listar, buscar, iniciar e finalizar sessões. Feito para facilitar o agendamento e acompanhamento das interpretações.")
@SecurityRequirement(name = "Bearer Authentication")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Criar sessão", description = "Cria uma nova sessão de interpretação em Libras. Informe o solicitante e o intérprete. O status inicial é PENDENTE.")
    public ResponseEntity<Session> createSession(@Valid @RequestBody Session session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createSession(session));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Buscar sessão por ID", description = "Busca uma sessão específica pelo ID. Mostra todos os detalhes da sessão.")
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        return sessionService.getSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar sessões", description = "Lista todas as sessões cadastradas no sistema.")
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Iniciar sessão", description = "Inicia uma sessão de interpretação. Só o intérprete pode iniciar. O status muda para CONECTADO.")
    public ResponseEntity<Session> startSession(@PathVariable Long id) {
        return sessionService.startSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/finish")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Finalizar sessão", description = "Finaliza uma sessão de interpretação. Só o intérprete pode finalizar. O status muda para FINALIZADO.")
    public ResponseEntity<Session> finishSession(@PathVariable Long id) {
        return sessionService.finishSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
