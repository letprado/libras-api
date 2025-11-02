package com.librasja.libras_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
@Schema(description = "Representa uma sessão de interpretação em Libras")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da sessão", example = "1")
    private Long id;

    @Column(name = "requester_id", nullable = false)
    @Schema(description = "ID do solicitante da sessão", example = "1")
    private Long requesterId;

    @Column(name = "interpreter_id", nullable = false)
    @Schema(description = "ID do intérprete responsável", example = "2")
    private Long interpreterId;

    @Column(nullable = false)
    @Schema(description = "Status da sessão: PENDENTE, CONECTADO, FINALIZADO, CANCELADO", example = "PENDENTE")
    private String status;

    @Column(name = "started_at")
    @Schema(description = "Data e hora de início da sessão", example = "2025-11-01T20:00:00")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    @Schema(description = "Data e hora de término da sessão", example = "2025-11-01T21:00:00")
    private LocalDateTime endedAt;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Data e hora de criação da sessão", example = "2025-11-01T19:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();

    public Long getId() { return id; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public Long getInterpreterId() { return interpreterId; }
    public void setInterpreterId(Long interpreterId) { this.interpreterId = interpreterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
}
