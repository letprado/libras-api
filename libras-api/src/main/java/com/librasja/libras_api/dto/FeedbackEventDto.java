package com.librasja.libras_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackEventDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long feedbackId;
    private Long sessionId;
    private Integer rating;
    private String comentario;
    private LocalDateTime createdAt;
}
