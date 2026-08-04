package com.naim.school.academicsession;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {

    boolean existsBySessionName(String sessionName);

    boolean existsBySessionNameAndIdNot(String sessionName, Long id);

    Optional<AcademicSession> findFirstByCurrentSessionTrue();

}