package com.librasja.libras_api.controller;

import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        return ResponseEntity.ok(sessionService.createSession(session));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        return sessionService.getSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Session> startSession(@PathVariable Long id) {
        return sessionService.getSession(id).map(session -> {
            session.setStatus("CONECTADO");
            session.setStartedAt(java.time.LocalDateTime.now());
            return ResponseEntity.ok(sessionService.updateSession(session));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<Session> finishSession(@PathVariable Long id) {
        return sessionService.getSession(id).map(session -> {
            session.setStatus("FINALIZADO");
            session.setEndedAt(java.time.LocalDateTime.now());
            return ResponseEntity.ok(sessionService.updateSession(session));
        }).orElse(ResponseEntity.notFound().build());
    }
}
