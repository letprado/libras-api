package com.librasja.libras_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSessionResponseDto {

    private Long scheduleId;
    private Long requesterId;
    private String requesterName;
    private Long interpreterId;
    private String interpreterName;
    private LocalDateTime scheduledFor;
    private Integer durationMinutes;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
