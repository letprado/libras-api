package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.SessionEventDto;
import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.messaging.EventPublisher;
import com.librasja.libras_api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventPublisher eventPublisher;

    public Session createSession(Session session) {
        validateSession(session);
        session.setStatus("PENDENTE");
        Session savedSession = sessionRepository.save(session);

        publishSessionCreatedEvent(savedSession);

        return savedSession;
    }

    public Optional<Session> getSession(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session updateSession(Session session) {
        session.setUpdatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public Optional<Session> startSession(Long id) {
        return sessionRepository.findById(id).map(session -> {
            session.setStatus("CONECTADO");
            session.setStartedAt(LocalDateTime.now());
            session.setUpdatedAt(LocalDateTime.now());
            return sessionRepository.save(session);
        });
    }

    public Optional<Session> finishSession(Long id) {
        return sessionRepository.findById(id).map(session -> {
            session.setStatus("FINALIZADO");
            session.setEndedAt(LocalDateTime.now());
            session.setUpdatedAt(LocalDateTime.now());
            Session updatedSession = sessionRepository.save(session);

            publishSessionFinishedEvent(updatedSession);

            return updatedSession;
        });
    }

    private void validateSession(Session session) {
        if (session.getRequesterId() == null) {
            throw new IllegalArgumentException("Requester ID é obrigatório");
        }
        if (session.getInterpreterId() == null) {
            throw new IllegalArgumentException("Interpreter ID é obrigatório");
        }
    }

    private void publishSessionCreatedEvent(Session session) {
        SessionEventDto event = SessionEventDto.builder()
                .sessionId(session.getId())
                .requesterId(session.getRequesterId())
                .interpreterId(session.getInterpreterId())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .eventType("CREATED")
                .build();
        eventPublisher.publishSessionCreatedEvent(event);
    }

    private void publishSessionFinishedEvent(Session session) {
        SessionEventDto event = SessionEventDto.builder()
                .sessionId(session.getId())
                .requesterId(session.getRequesterId())
                .interpreterId(session.getInterpreterId())
                .status(session.getStatus())
                .finishedAt(session.getEndedAt())
                .eventType("FINISHED")
                .build();
        eventPublisher.publishSessionFinishedEvent(event);
    }
}
