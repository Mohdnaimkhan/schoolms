package com.naim.school.session.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.session.entity.AcademicSession;
import com.naim.school.session.repository.AcademicSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicSessionService {

    private final AcademicSessionRepository repository;

    public List<AcademicSession> getAllSessions() {

        return repository.findAll();

    }

    public AcademicSession getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found."));

    }

    public void save(AcademicSession session) {

        // Only one active session

        if (Boolean.TRUE.equals(session.getActive())) {

            repository.findByActiveTrue().ifPresent(oldSession -> {

                if (!oldSession.getId().equals(session.getId())) {

                    oldSession.setActive(false);

                    repository.save(oldSession);

                }

            });

        }

        repository.save(session);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }

}