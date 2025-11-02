package com.librasja.libras_api.controller;

import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/feedbacks")
@Tag(name = "Feedbacks", description = "API para gerenciamento de feedbacks das sessões de interpretação")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(
        summary = "Criar Feedback",
        description = "Cria um novo feedback para uma sessão de interpretação. O rating deve estar entre 1 e 5 estrelas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Feedback criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Feedback.class))
        ),
        @ApiResponse(responseCode = "400", description = "Rating inválido (deve estar entre 1 e 5)")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Feedback>> createFeedback(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do feedback a ser criado", required = true)
        @RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        EntityModel<Feedback> resource = EntityModel.of(createdFeedback,
            linkTo(methodOn(FeedbackController.class).getAllFeedbacks()).withRel("all-feedbacks")
        );
        return ResponseEntity.ok(resource);
    }

    @Operation(
        summary = "Listar Todos os Feedbacks",
        description = "Retorna uma lista com todos os feedbacks cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de feedbacks retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Feedback>>> getAllFeedbacks() {
        List<EntityModel<Feedback>> feedbacks = feedbackService.getAllFeedbacks().stream()
            .map(feedback -> EntityModel.of(feedback,
                linkTo(methodOn(FeedbackController.class).getAllFeedbacks()).withSelfRel()
            ))
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Feedback>> resource = CollectionModel.of(feedbacks,
            linkTo(methodOn(FeedbackController.class).getAllFeedbacks()).withSelfRel()
        );
        return ResponseEntity.ok(resource);
    }
}
