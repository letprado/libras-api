package com.librasja.libras_api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "FEEDBACKS")
@Schema(description = "Representa um feedback/avaliação de uma sessão de interpretação")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do feedback", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false)
    @Schema(description = "Sessão relacionada a este feedback")
    private Session session;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    @Schema(description = "Avaliação de 1 a 5 estrelas", example = "5", minimum = "1", maximum = "5")
    private Integer rating;

    @Lob
    @Column(name = "comentario")
    @Schema(description = "Comentário opcional sobre a sessão", example = "Excelente atendimento!")
    private String comentario;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @Schema(description = "Data e hora de criação do feedback", example = "2025-11-01T21:30:00")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
