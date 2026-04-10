package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.Feedback;
import com.librasja.libras_api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySession(Session session);
}
