package com.librasja.libras_api.controller;

import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/sessions")
@Tag(name = "Sessões", description = "API para gerenciamento de sessões de interpretação em Libras")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(
        summary = "Criar Sessão",
        description = "Cria uma nova sessão de interpretação em Libras com status PENDENTE"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sessão criada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))
        )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Session>> createSession(
        @Parameter(description = "Dados da sessão a ser criada", required = true) 
        @RequestBody Session session) {
        Session createdSession = sessionService.createSession(session);
        EntityModel<Session> resource = EntityModel.of(createdSession,
            linkTo(methodOn(SessionController.class).getSession(createdSession.getId())).withSelfRel(),
            linkTo(methodOn(SessionController.class).getAllSessions()).withRel("all-sessions"),
            linkTo(methodOn(SessionController.class).startSession(createdSession.getId())).withRel("start"),
            linkTo(methodOn(SessionController.class).finishSession(createdSession.getId())).withRel("finish")
        );
        return ResponseEntity.ok(resource);
    }

    @Operation(
        summary = "Buscar Sessão por ID",
        description = "Retorna os detalhes de uma sessão específica pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão encontrada"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Session>> getSession(
        @Parameter(description = "ID da sessão", required = true, example = "1")
        @PathVariable Long id) {
        return sessionService.getSession(id).map(session -> {
            EntityModel<Session> resource = EntityModel.of(session,
                linkTo(methodOn(SessionController.class).getSession(id)).withSelfRel(),
                linkTo(methodOn(SessionController.class).getAllSessions()).withRel("all-sessions"),
                linkTo(methodOn(SessionController.class).startSession(id)).withRel("start"),
                linkTo(methodOn(SessionController.class).finishSession(id)).withRel("finish")
            );
            return ResponseEntity.ok(resource);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Listar Todas as Sessões",
        description = "Retorna uma lista com todas as sessões de interpretação cadastradas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sessões retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Session>>> getAllSessions() {
        List<EntityModel<Session>> sessions = sessionService.getAllSessions().stream()
            .map(session -> EntityModel.of(session,
                linkTo(methodOn(SessionController.class).getSession(session.getId())).withSelfRel(),
                linkTo(methodOn(SessionController.class).getAllSessions()).withRel("all-sessions")
            ))
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Session>> resource = CollectionModel.of(sessions,
            linkTo(methodOn(SessionController.class).getAllSessions()).withSelfRel()
        );
        return ResponseEntity.ok(resource);
    }

    @Operation(
        summary = "Iniciar Sessão",
        description = "Inicia uma sessão, alterando o status para CONECTADO e registrando o horário de início"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão iniciada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    @PostMapping("/{id}/start")
    public ResponseEntity<EntityModel<Session>> startSession(
        @Parameter(description = "ID da sessão a ser iniciada", required = true, example = "1")
        @PathVariable Long id) {
        return sessionService.getSession(id).map(session -> {
            session.setStatus("CONECTADO");
            session.setStartedAt(java.time.LocalDateTime.now());
            Session updatedSession = sessionService.updateSession(session);
            EntityModel<Session> resource = EntityModel.of(updatedSession,
                linkTo(methodOn(SessionController.class).getSession(id)).withSelfRel(),
                linkTo(methodOn(SessionController.class).getAllSessions()).withRel("all-sessions"),
                linkTo(methodOn(SessionController.class).finishSession(id)).withRel("finish")
            );
            return ResponseEntity.ok(resource);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Finalizar Sessão",
        description = "Finaliza uma sessão, alterando o status para FINALIZADO e registrando o horário de término"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão finalizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    @PostMapping("/{id}/finish")
    public ResponseEntity<EntityModel<Session>> finishSession(
        @Parameter(description = "ID da sessão a ser finalizada", required = true, example = "1")
        @PathVariable Long id) {
        return sessionService.getSession(id).map(session -> {
            session.setStatus("FINALIZADO");
            session.setEndedAt(java.time.LocalDateTime.now());
            Session updatedSession = sessionService.updateSession(session);
            EntityModel<Session> resource = EntityModel.of(updatedSession,
                linkTo(methodOn(SessionController.class).getSession(id)).withSelfRel(),
                linkTo(methodOn(SessionController.class).getAllSessions()).withRel("all-sessions")
            );
            return ResponseEntity.ok(resource);
        }).orElse(ResponseEntity.notFound().build());
    }
}
