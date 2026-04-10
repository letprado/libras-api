package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.SessionResponseDto;
import com.librasja.libras_api.feign.SessionServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionIntegrationService {

    private final SessionServiceClient sessionServiceClient;

    public SessionResponseDto getSessionVifeign(Long sessionId) {
        log.info("Chamando Feign cliente para obter sessão com ID: {}", sessionId);
        try {
            return sessionServiceClient.getSession(sessionId);
        } catch (Exception e) {
            log.error("Erro ao chamar serviço de sessão via Feign: {}", e.getMessage());
            throw new RuntimeException("Erro ao integrar com serviço de sessão", e);
        }
    }

    public List<SessionResponseDto> getAllSessionsViaFeign() {
        log.info("Chamando Feign cliente para obter todas as sessões");
        try {
            return sessionServiceClient.getAllSessions();
        } catch (Exception e) {
            log.error("Erro ao chamar serviço de sessões via Feign: {}", e.getMessage());
            throw new RuntimeException("Erro ao integrar com serviço de sessões", e);
        }
    }
}
