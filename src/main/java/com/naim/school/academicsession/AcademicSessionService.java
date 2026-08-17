package com.naim.school.academicsession;

import com.naim.school.sms.BusinessException;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicSessionService {

    private final AcademicSessionRepository repository;

    /*
     * ==========================================
     * GET ALL
     * ==========================================
     */
    public List<AcademicSession> getAllSessions() {
        return repository.findAll();
    }

    /*
     * ==========================================
     * GET BY ID
     * ==========================================
     */
    public AcademicSession getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Academic Session not found."));
    }

    /*
     * ==========================================
     * SAVE (ADD + EDIT)
     * ==========================================
     */
    @Transactional
    public void save(AcademicSession academicSession) {

        // Duplicate Session Name Validation
        if (academicSession.getId() == null) {

            if (repository.existsBySessionName(academicSession.getSessionName())) {
                throw new BusinessException("Session already exists.");
            }

        } else {

            if (repository.existsBySessionNameAndIdNot(
                    academicSession.getSessionName(),
                    academicSession.getId())) {

                throw new BusinessException("Session already exists.");
            }

        }

        /*
         * First Session -> Automatically Current
         */
        if (academicSession.getId() == null
                && repository.findFirstByCurrentSessionTrue().isEmpty()) {

            academicSession.setCurrentSession(true);

        }

        repository.save(academicSession);

    }

    /*
     * ==========================================
     * SET CURRENT SESSION
     * ==========================================
     */
    @Transactional
    public void setCurrentSession(Long id) {

        AcademicSession session = getById(id);

        // Closed Session cannot become Current
        if (session.getEndDate() != null) {
            throw new BusinessException("Closed Session cannot be Current.");
        }

        repository.findFirstByCurrentSessionTrue().ifPresent(oldSession -> {

            oldSession.setCurrentSession(false);

            repository.save(oldSession);

        });

        session.setCurrentSession(true);

        repository.save(session);

    }

    /*
     * ==========================================
     * GET CURRENT SESSION
     * ==========================================
     */
    public AcademicSession getCurrentSession() {

        return repository.findFirstByCurrentSessionTrue()
                .orElseThrow(() -> new BusinessException("No Current Session Found."));

    }

    /*
     * ==========================================
     * GET CURRENT SESSION (NULL-SAFE)
     * Used by places like the dashboard that must not
     * fail just because no session has been created yet
     * (e.g. right after a fresh install).
     * ==========================================
     */
    public AcademicSession getCurrentSessionOrNull() {

        return repository.findFirstByCurrentSessionTrue()
                .orElse(null);

    }

    /*
     * ==========================================
     * GET CURRENT SESSION (NULL-SAFE)
     * Used by places like the dashboard that must not
     * fail just because no session has been created yet
     * (e.g. right after a fresh install).
     * ==========================================
     */
    public AcademicSession getCurrentSessionOrNull() {

        return repository.findFirstByCurrentSessionTrue()
                .orElse(null);

    }

    /*
     * ==========================================
     * CLOSE SESSION
     * ==========================================
     */
    @Transactional
    public void closeSession(Long id, LocalDate endDate) {

        AcademicSession academicSession = getById(id);

        if (academicSession.getEndDate() != null) {
            throw new BusinessException("Session already closed.");
        }

        if (endDate == null) {
            throw new BusinessException("End Date is required.");
        }

        if (endDate.isBefore(academicSession.getStartDate())) {
            throw new BusinessException("End Date cannot be before Start Date.");
        }

        academicSession.setEndDate(endDate);

        academicSession.setCurrentSession(false);

        repository.save(academicSession);

    }

}