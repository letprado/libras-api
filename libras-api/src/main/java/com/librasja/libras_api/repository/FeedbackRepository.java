package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySession(Session session);

    @Query("""
            SELECT f FROM Feedback f
            WHERE f.session.interpreterId = :interpreterId
              AND f.session.createdAt BETWEEN :periodStart AND :periodEnd
            """)
    List<Feedback> findByInterpreterIdAndSessionCreatedAtBetween(
            @Param("interpreterId") Long interpreterId,
            @Param("periodStart") LocalDateTime periodStart,
            @Param("periodEnd") LocalDateTime periodEnd
    );
}
