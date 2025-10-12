package com.librasja.libras_api.service;

import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Optional<Session> getSession(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session updateSession(Session session) {
        return sessionRepository.save(session);
    }
}
