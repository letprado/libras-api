package com.librasja.libras_api.controller;

import com.librasja.libras_api.dto.ReportRequestDto;
import com.librasja.libras_api.dto.ReportResponseDto;
import com.librasja.libras_api.service.ReportService;
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
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Gerenciamento de relatórios de interpretação")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Gerar relatório de interpretação", description = "Gera um fluxo completo de relatório análisando sessões e feedbacks")
    public ResponseEntity<ReportResponseDto> generateReport(@Valid @RequestBody ReportRequestDto requestDto) {
        ReportResponseDto report = reportService.generateInterpretationReport(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/{reportId}")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Obter relatório por ID", description = "Retorna os detalhes de um relatório específico")
    public ResponseEntity<ReportResponseDto> getReportById(@PathVariable Long reportId) {
        ReportResponseDto report = reportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/interpreter/{interpreterId}")
    @PreAuthorize("hasRole('INTERPRETER')")
    @Operation(summary = "Listar relatórios do intérprete", description = "Retorna todos os relatórios de um intérprete específico")
    public ResponseEntity<List<ReportResponseDto>> getReportsByInterpreter(@PathVariable Long interpreterId) {
        List<ReportResponseDto> reports = reportService.getReportsByInterpreter(interpreterId);
        return ResponseEntity.ok(reports);
    }
}
