package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByInterpreterIdAndCreatedAtBetween(
            Long interpreterId,
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    );
}
