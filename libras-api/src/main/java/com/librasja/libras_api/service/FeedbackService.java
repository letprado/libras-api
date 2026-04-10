package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.FeedbackEventDto;
import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.messaging.EventPublisher;
import com.librasja.libras_api.repository.FeedbackRepository;
import com.librasja.libras_api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SessionRepository sessionRepository;
    private final EventPublisher eventPublisher;

    public Feedback createFeedback(Feedback feedback) {
        validateFeedback(feedback);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        publishFeedbackCreatedEvent(savedFeedback);

        return savedFeedback;
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbacksBySession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        return feedbackRepository.findBySession(session);
    }

    private void validateFeedback(Feedback feedback) {
        if (feedback.getSession() == null || feedback.getSession().getId() == null) {
            throw new IllegalArgumentException("Session ID é obrigatória");
        }

        if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new IllegalArgumentException("Rating deve estar entre 1 e 5");
        }

        if (feedback.getComentario() == null || feedback.getComentario().trim().isEmpty()) {
            throw new IllegalArgumentException("Comentário é obrigatório");
        }

        if (feedback.getComentario().length() > 1000) {
            throw new IllegalArgumentException("Comentário não pode exceder 1000 caracteres");
        }
    }

    private void publishFeedbackCreatedEvent(Feedback feedback) {
        FeedbackEventDto event = FeedbackEventDto.builder()
                .feedbackId(feedback.getId())
                .sessionId(feedback.getSession().getId())
                .rating(feedback.getRating())
                .comentario(feedback.getComentario())
                .createdAt(feedback.getCreatedAt())
                .build();
        eventPublisher.publishFeedbackCreatedEvent(event);
    }
}
