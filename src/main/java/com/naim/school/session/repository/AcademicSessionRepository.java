package com.naim.school.session.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.session.entity.AcademicSession;

public interface AcademicSessionRepository
        extends JpaRepository<AcademicSession, Long> {

    Optional<AcademicSession> findByActiveTrue();

}