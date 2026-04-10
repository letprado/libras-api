package com.librasja.libras_api.controller;

import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.service.FeedbackService;
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
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedbacks", description = "Permite avaliar as sessões de interpretação. Aqui você pode criar e listar feedbacks.")
@SecurityRequirement(name = "Bearer Authentication")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Criar feedback", description = "Cria um feedback para uma sessão com nota e comentário.")
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(feedback));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar feedbacks", description = "Lista todos os feedbacks cadastrados no sistema.")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar feedbacks por sessão", description = "Lista todos os feedbacks de uma sessão específica.")
    public ResponseEntity<List<Feedback>> getFeedbacksBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksBySession(sessionId));
    }
}
