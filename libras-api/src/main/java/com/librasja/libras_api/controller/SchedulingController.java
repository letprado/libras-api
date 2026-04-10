package com.librasja.libras_api.controller;

import com.librasja.libras_api.dto.ScheduleSessionRequestDto;
import com.librasja.libras_api.dto.ScheduleSessionResponseDto;
import com.librasja.libras_api.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduling")
@RequiredArgsConstructor
@Tag(name = "Scheduling", description = "Agendamento de sessões de interpretação")
@SecurityRequirement(name = "Bearer Authentication")
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('REQUESTER')")
    @Operation(summary = "Agendar nova sessão", description = "Permite que REQUESTER agende uma sessão com um INTERPRETER")
    public ResponseEntity<ScheduleSessionResponseDto> scheduleSession(@Valid @RequestBody ScheduleSessionRequestDto requestDto) {
        ScheduleSessionResponseDto response = schedulingService.scheduleSession(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{scheduleId}/confirm")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Confirmar agendamento", description = "Permite que INTERPRETER confirme um agendamento")
    public ResponseEntity<ScheduleSessionResponseDto> confirmSchedule(@PathVariable Long scheduleId) {
        ScheduleSessionResponseDto response = schedulingService.confirmSchedule(scheduleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{scheduleId}/cancel")
    @PreAuthorize("hasAnyRole('REQUESTER', 'INTERPRETER')")
    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento de sessão")
    public ResponseEntity<String> cancelSchedule(@PathVariable Long scheduleId) {
        schedulingService.cancelSchedule(scheduleId);
        return ResponseEntity.ok("Agendamento cancelado com sucesso");
    }

    @GetMapping("/interpreter/{interpreterId}")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Listar agendamentos do intérprete", description = "Retorna todos os agendamentos de um intérprete")
    public ResponseEntity<List<ScheduleSessionResponseDto>> getSchedulesByInterpreter(@PathVariable Long interpreterId) {
        List<ScheduleSessionResponseDto> schedules = schedulingService.getSchedulesByInterpreter(interpreterId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/requester/{requesterId}")
    @PreAuthorize("hasRole('REQUESTER')")
    @Operation(summary = "Listar agendamentos do solicitante", description = "Retorna todos os agendamentos de um solicitante")
    public ResponseEntity<List<ScheduleSessionResponseDto>> getSchedulesByRequester(@PathVariable Long requesterId) {
        List<ScheduleSessionResponseDto> schedules = schedulingService.getSchedulesByRequester(requesterId);
        return ResponseEntity.ok(schedules);
    }
}
