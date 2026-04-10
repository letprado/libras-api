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
public class ReportResponseDto {

    private Long reportId;
    private Long interpreterId;
    private String interpreterName;
    private Integer totalSessions;
    private Integer totalFeedbacks;
    private Double averageRating;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String status;
    private LocalDateTime generatedAt;
}
