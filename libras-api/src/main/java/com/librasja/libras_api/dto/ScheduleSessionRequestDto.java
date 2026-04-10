package com.librasja.libras_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSessionRequestDto {

    @NotNull(message = "Requester ID é obrigatório")
    private Long requesterId;

    @NotNull(message = "Interpreter ID é obrigatório")
    private Long interpreterId;

    @NotNull(message = "Data/hora agendada é obrigatória")
    private LocalDateTime scheduledFor;

    @NotNull(message = "Duração em minutos é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    private Integer durationMinutes;

    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String description;
}
