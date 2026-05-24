package com.librasja.libras_api.repository;

import com.librasja.libras_api.entity.InterpreterProfile;
import com.librasja.libras_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterpreterProfileRepository extends JpaRepository<InterpreterProfile, Long> {
    Optional<InterpreterProfile> findByInterpreter(User interpreter);
}
