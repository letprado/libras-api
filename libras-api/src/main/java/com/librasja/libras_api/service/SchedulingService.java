package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.ScheduleSessionRequestDto;
import com.librasja.libras_api.dto.ScheduleSessionResponseDto;
import com.librasja.libras_api.entity.SessionSchedule;
import com.librasja.libras_api.entity.User;
import com.librasja.libras_api.repository.SessionScheduleRepository;
import com.librasja.libras_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SchedulingService {

    private final SessionScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleSessionResponseDto scheduleSession(ScheduleSessionRequestDto requestDto) {
        log.info("Iniciando agendamento de sessão para requester {} e interpreter {}",
                requestDto.getRequesterId(), requestDto.getInterpreterId());

        // Etapa 1: Validar dados de entrada
        validateScheduleRequest(requestDto);

        // Etapa 2: Buscar usuários
        User requester = userRepository.findById(requestDto.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado"));

        User interpreter = userRepository.findById(requestDto.getInterpreterId())
                .orElseThrow(() -> new IllegalArgumentException("Intérprete não encontrado"));

        // Validar se são os roles corretos
        if (!requester.getRole().toString().equals("REQUESTER")) {
            throw new IllegalArgumentException("Usuário não é um solicitante");
        }

        if (!interpreter.getRole().toString().equals("INTERPRETER")) {
            throw new IllegalArgumentException("Usuário não é um intérprete");
        }

        // Etapa 3: Validar disponibilidade do intérprete
        checkInterpreterAvailability(interpreter, requestDto.getScheduledFor(), requestDto.getDurationMinutes());

        // Etapa 4: Validar se a data é futura
        if (!requestDto.getScheduledFor().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data agendada deve ser no futuro");
        }

        // Etapa 5: Criar agendamento
        SessionSchedule schedule = SessionSchedule.builder()
                .requester(requester)
                .interpreter(interpreter)
                .scheduledFor(requestDto.getScheduledFor())
                .durationMinutes(requestDto.getDurationMinutes())
                .description(requestDto.getDescription())
                .status(SessionSchedule.ScheduleStatus.SCHEDULED)
                .build();

        SessionSchedule savedSchedule = scheduleRepository.save(schedule);
        log.info("Agendamento criado com sucesso: scheduleId={}, scheduledFor={}", savedSchedule.getId(), savedSchedule.getScheduledFor());

        // Etapa 6: Publicar evento (poderia ser assíncrono com RabbitMQ)
        // publishScheduleCreatedEvent(savedSchedule);

        // Etapa 7: Retornar resposta
        return mapToResponseDto(savedSchedule);
    }

    public ScheduleSessionResponseDto confirmSchedule(Long scheduleId) {
        log.info("Confirmando agendamento: {}", scheduleId);

        SessionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (!schedule.getStatus().equals(SessionSchedule.ScheduleStatus.SCHEDULED)) {
            throw new IllegalArgumentException("Apenas agendamentos SCHEDULED podem ser confirmados");
        }

        schedule.setStatus(SessionSchedule.ScheduleStatus.CONFIRMED);
        schedule.setUpdatedAt(LocalDateTime.now());

        SessionSchedule updatedSchedule = scheduleRepository.save(schedule);
        log.info("Agendamento confirmado: {}", scheduleId);

        return mapToResponseDto(updatedSchedule);
    }

    public void cancelSchedule(Long scheduleId) {
        log.info("Cancelando agendamento: {}", scheduleId);

        SessionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (schedule.getStatus().equals(SessionSchedule.ScheduleStatus.COMPLETED)) {
            throw new IllegalArgumentException("Agendamentos COMPLETED não podem ser cancelados");
        }

        schedule.setStatus(SessionSchedule.ScheduleStatus.CANCELLED);
        schedule.setUpdatedAt(LocalDateTime.now());

        scheduleRepository.save(schedule);
    }

    public List<ScheduleSessionResponseDto> getSchedulesByInterpreter(Long interpreterId) {
        log.info("Listando agendamentos do intérprete: {}", interpreterId);

        User interpreter = userRepository.findById(interpreterId)
                .orElseThrow(() -> new IllegalArgumentException("Intérprete não encontrado"));

        List<SessionSchedule> schedules = scheduleRepository.findByInterpreter(interpreter);
        return schedules.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<ScheduleSessionResponseDto> getSchedulesByRequester(Long requesterId) {
        log.info("Listando agendamentos do solicitante: {}", requesterId);

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado"));

        List<SessionSchedule> schedules = scheduleRepository.findByRequester(requester);
        return schedules.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private void validateScheduleRequest(ScheduleSessionRequestDto requestDto) {
        if (requestDto.getScheduledFor() == null) {
            throw new IllegalArgumentException("Data/hora agendada é obrigatória");
        }

        if (requestDto.getDurationMinutes() == null || requestDto.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duração deve ser um valor positivo");
        }

        if (requestDto.getDurationMinutes() > 480) { // 8 horas
            throw new IllegalArgumentException("Duração máxima é 480 minutos (8 horas)");
        }
    }

    private void checkInterpreterAvailability(User interpreter, LocalDateTime scheduledFor, Integer durationMinutes) {
        LocalDateTime endTime = scheduledFor.plusMinutes(durationMinutes);

        List<SessionSchedule> conflictingSchedules = scheduleRepository
                .findByInterpreterAndScheduledForBetween(interpreter, scheduledFor, endTime)
                .stream()
                .filter(s -> !s.getStatus().equals(SessionSchedule.ScheduleStatus.CANCELLED))
                .toList();

        if (!conflictingSchedules.isEmpty()) {
            throw new IllegalArgumentException("Intérprete não está disponível no horário solicitado");
        }
    }

    private ScheduleSessionResponseDto mapToResponseDto(SessionSchedule schedule) {
        return ScheduleSessionResponseDto.builder()
                .scheduleId(schedule.getId())
                .requesterId(schedule.getRequester().getId())
                .requesterName(schedule.getRequester().getUsername())
                .interpreterId(schedule.getInterpreter().getId())
                .interpreterName(schedule.getInterpreter().getUsername())
                .scheduledFor(schedule.getScheduledFor())
                .durationMinutes(schedule.getDurationMinutes())
                .description(schedule.getDescription())
                .status(schedule.getStatus().toString())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}
