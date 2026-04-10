package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.InterpretationReport;
import com.librasja.libras_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterpretationReportRepository extends JpaRepository<InterpretationReport, Long> {
    List<InterpretationReport> findByInterpreter(User interpreter);
    Optional<InterpretationReport> findByInterpreterAndPeriodStartAndPeriodEnd(User interpreter, LocalDateTime startDate, LocalDateTime endDate);
}
