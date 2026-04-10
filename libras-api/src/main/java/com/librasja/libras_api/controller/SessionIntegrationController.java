package com.librasja.libras_api.controller;

import com.librasja.libras_api.dto.SessionResponseDto;
import com.librasja.libras_api.service.SessionIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/external")
@RequiredArgsConstructor
@Tag(name = "Session Integration (Feign)", description = "Integração com serviço de sessões via Feign")
@SecurityRequirement(name = "Bearer Authentication")
public class SessionIntegrationController {

    private final SessionIntegrationService sessionIntegrationService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('REQUESTER', 'INTERPRETER')")
    @Operation(summary = "Obter sessão via Feign", description = "Chamada síncrona para obter sessão através do Feign client")
    public ResponseEntity<SessionResponseDto> getSessionViaFeign(@PathVariable Long id) {
        SessionResponseDto sessionDto = sessionIntegrationService.getSessionVifeign(id);
        return ResponseEntity.ok(sessionDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('REQUESTER', 'INTERPRETER')")
    @Operation(summary = "Listar todas as sessões via Feign", description = "Chamada síncrona para listar sessões através do Feign client")
    public ResponseEntity<List<SessionResponseDto>> getAllSessionsViaFeign() {
        List<SessionResponseDto> sessions = sessionIntegrationService.getAllSessionsViaFeign();
        return ResponseEntity.ok(sessions);
    }
}
