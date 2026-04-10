package com.librasja.libras_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interpretation_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterpretationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "interpreter_id", nullable = false)
    private User interpreter;

    @Builder.Default
    @Column(name = "total_sessions", nullable = false)
    private Integer totalSessions = 0;

    @Builder.Default
    @Column(name = "total_feedbacks", nullable = false)
    private Integer totalFeedbacks = 0;

    @Builder.Default
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    @Builder.Default
    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    public enum ReportStatus {
        PENDING,
        GENERATING,
        COMPLETED,
        ERROR
    }
}
