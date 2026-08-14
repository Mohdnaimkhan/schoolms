package com.naim.school.certificate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.student.Student;
import com.naim.school.student.StudentService;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;
import com.naim.school.studentsession.StudentSessionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final StudentService studentService;
    private final StudentSessionService studentSessionService;
    private final StudentSessionRepo studentSessionRepo;
    private final TransferCertificateRepository tcRepository;

    /*
     * ==========================================================
     * BONAFIDE / CHARACTER - use the student's current placement,
     * falling back to their most recent historical one if they no
     * longer have an active "current" session (e.g. already left).
     * ==========================================================
     */

    public StudentSession getRelevantSession(Long studentId) {

        Student student = studentService.getById(studentId);

        StudentSession current = studentSessionService.getCurrentSession(student);

        if (current != null) {

            return current;

        }

        List<StudentSession> history = studentSessionService.getStudentHistory(student);

        return history.isEmpty() ? null : history.get(0);

    }

    /*
     * ==========================================================
     * TRANSFER CERTIFICATE
     * ==========================================================
     */

    public List<TransferCertificate> getTcHistory(Long studentId) {

        Student student = studentService.getById(studentId);

        return tcRepository.findByStudentSession_Student(student);

    }

    public TransferCertificate getTcById(Long id) {

        return tcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer Certificate not found."));

    }

    public TransferCertificate save(TransferCertificate tc) {

        if (tc.getStudentSessionId() == null) {

            throw new RuntimeException("Student is required.");

        }

        StudentSession studentSession = studentSessionRepo.findById(tc.getStudentSessionId())
                .orElseThrow(() -> new RuntimeException("Student session not found."));

        tc.setStudentSession(studentSession);

        if (tc.getTcNumber() == null || tc.getTcNumber().isBlank()) {

            tc.setTcNumber("TC-" + System.currentTimeMillis());

        } else if (tc.getId() == null && tcRepository.existsByTcNumber(tc.getTcNumber())) {

            throw new RuntimeException("This TC Number is already used.");

        }

        return tcRepository.save(tc);

    }

}
