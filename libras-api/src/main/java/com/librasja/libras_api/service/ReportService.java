package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.ReportRequestDto;
import com.librasja.libras_api.dto.ReportResponseDto;
import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.entity.InterpretationReport;
import com.librasja.libras_api.entity.Session;
import com.librasja.libras_api.entity.User;
import com.librasja.libras_api.repository.FeedbackRepository;
import com.librasja.libras_api.repository.InterpretationReportRepository;
import com.librasja.libras_api.repository.SessionRepository;
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
public class ReportService {

    private final InterpretationReportRepository reportRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final FeedbackRepository feedbackRepository;

    public ReportResponseDto generateInterpretationReport(ReportRequestDto requestDto) {
        log.info("Iniciando geração de relatório para intérprete: {}", requestDto.getInterpreterId());

        // Etapa 1: Validar dados de entrada
        validateReportRequest(requestDto);

        // Etapa 2: Buscar usuário intérprete
        User interpreter = userRepository.findById(requestDto.getInterpreterId())
                .orElseThrow(() -> new IllegalArgumentException("Intérprete não encontrado"));

        // Etapa 3: Validar se é realmente um intérprete
        if (!interpreter.getRole().toString().equals("INTERPRETER")) {
            throw new IllegalArgumentException("Usuário não é um intérprete");
        }

        // Etapa 4: Consultar sessões do período
        List<Session> sessions = findSessionsInPeriod(
                interpreter.getId(),
                requestDto.getPeriodStart(),
                requestDto.getPeriodEnd()
        );

        // Etapa 5: Calcular estatísticas
        int totalSessions = sessions.size();
        List<Feedback> feedbacks = collectFeedbacksFromSessions(sessions);
        int totalFeedbacks = feedbacks.size();
        double averageRating = calculateAverageRating(feedbacks);

        // Etapa 6: Verificar se já existe relatório para este período
        Optional<InterpretationReport> existingReport = reportRepository
                .findByInterpreterAndPeriodStartAndPeriodEnd(
                        interpreter,
                        requestDto.getPeriodStart(),
                        requestDto.getPeriodEnd()
                );

        InterpretationReport report;
        if (existingReport.isPresent()) {
            report = existingReport.get();
            report.setTotalSessions(totalSessions);
            report.setTotalFeedbacks(totalFeedbacks);
            report.setAverageRating(averageRating);
            report.setStatus(InterpretationReport.ReportStatus.COMPLETED);
        } else {
            report = InterpretationReport.builder()
                    .interpreter(interpreter)
                    .totalSessions(totalSessions)
                    .totalFeedbacks(totalFeedbacks)
                    .averageRating(averageRating)
                    .periodStart(requestDto.getPeriodStart())
                    .periodEnd(requestDto.getPeriodEnd())
                    .status(InterpretationReport.ReportStatus.COMPLETED)
                    .build();
        }

        // Etapa 7: Salvar relatório
        InterpretationReport savedReport = reportRepository.save(report);
        log.info("Relatório gerado com sucesso: reportId={}, totalSessions={}, averageRating={}",
                savedReport.getId(), totalSessions, averageRating);

        // Retornar resposta
        return mapToResponseDto(savedReport);
    }

    public ReportResponseDto getReportById(Long reportId) {
        log.info("Buscando relatório: {}", reportId);
        InterpretationReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado"));
        return mapToResponseDto(report);
    }

    public List<ReportResponseDto> getReportsByInterpreter(Long interpreterId) {
        log.info("Listando relatórios do intérprete: {}", interpreterId);
        User interpreter = userRepository.findById(interpreterId)
                .orElseThrow(() -> new IllegalArgumentException("Intérprete não encontrado"));

        List<InterpretationReport> reports = reportRepository.findByInterpreter(interpreter);
        return reports.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private void validateReportRequest(ReportRequestDto requestDto) {
        if (requestDto.getPeriodStart().isAfter(requestDto.getPeriodEnd())) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }

        LocalDateTime now = LocalDateTime.now();
        if (requestDto.getPeriodEnd().isAfter(now)) {
            throw new IllegalArgumentException("Data de fim não pode ser no futuro");
        }
    }

    private List<Session> findSessionsInPeriod(Long interpreterId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Session> allSessions = sessionRepository.findAll();
        return allSessions.stream()
                .filter(s -> s.getInterpreterId().equals(interpreterId))
                .filter(s -> s.getCreatedAt() != null && !s.getCreatedAt().isBefore(startDate) && !s.getCreatedAt().isAfter(endDate))
                .toList();
    }

    private List<Feedback> collectFeedbacksFromSessions(List<Session> sessions) {
        return sessions.stream()
                .flatMap(session -> feedbackRepository.findBySession(session).stream())
                .toList();
    }

    private double calculateAverageRating(List<Feedback> feedbacks) {
        if (feedbacks.isEmpty()) {
            return 0.0;
        }
        return feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    private ReportResponseDto mapToResponseDto(InterpretationReport report) {
        return ReportResponseDto.builder()
                .reportId(report.getId())
                .interpreterId(report.getInterpreter().getId())
                .interpreterName(report.getInterpreter().getUsername())
                .totalSessions(report.getTotalSessions())
                .totalFeedbacks(report.getTotalFeedbacks())
                .averageRating(report.getAverageRating())
                .periodStart(report.getPeriodStart())
                .periodEnd(report.getPeriodEnd())
                .status(report.getStatus().toString())
                .generatedAt(report.getGeneratedAt())
                .build();
    }
}
