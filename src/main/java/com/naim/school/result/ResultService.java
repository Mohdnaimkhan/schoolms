package com.naim.school.result;

import com.naim.school.sms.BusinessException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.naim.school.exam.Exam;
import com.naim.school.exam.ExamRepository;
import com.naim.school.exam.ExamSubject;
import com.naim.school.exam.ExamSubjectRepository;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;
import com.naim.school.studentsession.StudentSessionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final ExamRepository examRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final StudentSessionRepo studentSessionRepo;
    private final StudentSessionService studentSessionService;

    /*
     * ==========================================================
     * MARKS ENTRY - one subject, whole class, at a time
     * ==========================================================
     */

    public ExamSubject getExamSubject(Long examSubjectId) {

        return examSubjectRepository.findById(examSubjectId)
                .orElseThrow(() -> new BusinessException("Exam subject not found."));

    }

    // Students of the exam's class/session, plus any marks already entered for this subject
    public List<StudentSession> getStudentsForEntry(ExamSubject examSubject) {

        Exam exam = examSubject.getExam();

        return studentSessionService.getStudentsBySessionAndClass(
                exam.getAcademicSession().getId(),
                exam.getClassRoom().getId());

    }

    public Map<Long, BigDecimal> getExistingMarks(ExamSubject examSubject) {

        Map<Long, BigDecimal> marksByStudentSession = new HashMap<>();

        for (Result result : resultRepository.findByExamSubject(examSubject)) {

            if (result.getStudentSession() != null) {

                marksByStudentSession.put(
                        result.getStudentSession().getId(),
                        result.getMarksObtained());

            }

        }

        return marksByStudentSession;

    }

    // Bulk save - one entry per student, in the same order the form rows were rendered.
    // Marks arrive as raw strings so a blank input (student absent / not entered
    // yet) can be safely skipped instead of failing the whole submission.
    public void saveMarks(Long examSubjectId, List<Long> studentSessionIds, List<String> marks) {

        ExamSubject examSubject = getExamSubject(examSubjectId);

        if (studentSessionIds == null || marks == null
                || studentSessionIds.size() != marks.size()) {

            throw new BusinessException("Marks could not be saved - form data was incomplete.");

        }

        for (int i = 0; i < studentSessionIds.size(); i++) {

            Long studentSessionId = studentSessionIds.get(i);
            String rawMarks = marks.get(i);

            if (rawMarks == null || rawMarks.isBlank()) {

                continue;

            }

            BigDecimal marksObtained;

            try {

                marksObtained = new BigDecimal(rawMarks.trim());

            } catch (NumberFormatException ex) {

                continue;

            }

            StudentSession studentSession = studentSessionRepo.findById(studentSessionId)
                    .orElseThrow(() -> new BusinessException("Student session not found."));

            Result result = resultRepository
                    .findByStudentSessionAndExamSubject(studentSession, examSubject)
                    .orElse(new Result());

            result.setStudentSession(studentSession);
            result.setExamSubject(examSubject);
            result.setMarksObtained(marksObtained);

            resultRepository.save(result);

        }

    }

    /*
     * ==========================================================
     * REPORT CARD
     * ==========================================================
     */

    public List<Result> getReportCard(Long studentSessionId, Long examId) {

        StudentSession studentSession = studentSessionRepo.findById(studentSessionId)
                .orElseThrow(() -> new BusinessException("Student session not found."));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new BusinessException("Exam not found."));

        return resultRepository.findByStudentSessionAndExamSubject_Exam(
                studentSession,
                exam);

    }

}
