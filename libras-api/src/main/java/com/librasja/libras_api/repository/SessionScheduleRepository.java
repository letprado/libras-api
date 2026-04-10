package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.SessionSchedule;
import com.librasja.libras_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionScheduleRepository extends JpaRepository<SessionSchedule, Long> {
    List<SessionSchedule> findByRequester(User requester);
    List<SessionSchedule> findByInterpreter(User interpreter);
    List<SessionSchedule> findByInterpreterAndScheduledForBetween(User interpreter, LocalDateTime start, LocalDateTime end);
    List<SessionSchedule> findByRequesterAndScheduledForBetween(User requester, LocalDateTime start, LocalDateTime end);
}
