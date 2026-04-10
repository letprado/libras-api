package com.librasja.libras_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDto {

    @NotNull(message = "Interpreter ID é obrigatório")
    private Long interpreterId;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime periodStart;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime periodEnd;
}
