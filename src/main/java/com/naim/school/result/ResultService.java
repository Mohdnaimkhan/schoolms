package com.naim.school.result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naim.school.exam.Exam;
import com.naim.school.exam.ExamRepository;
import com.naim.school.exam.ExamSubject;
import com.naim.school.exam.ExamSubjectRepository;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;
import com.naim.school.studentsession.StudentSessionService;
import com.naim.school.security.CurrentUserService;
import com.naim.school.security.Role;
import com.naim.school.teachersession.TeacherSessionRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final ExamRepository examRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final StudentSessionRepo studentSessionRepo;
    private final StudentSessionService studentSessionService;
    private final CurrentUserService currentUserService;
    private final TeacherSessionRepo teacherSessionRepo;

    /*
     * ==========================================================
     * MARKS ENTRY - one subject, whole class, at a time
     * ==========================================================
     */

    public ExamSubject getExamSubject(Long examSubjectId) {

        return examSubjectRepository.findById(examSubjectId)
                .orElseThrow(() -> new RuntimeException("Exam subject not found."));

    }

    // Students of the exam's class/session, plus any marks already entered for this subject
    public List<StudentSession> getStudentsForEntry(ExamSubject examSubject) {

        authorizeExamSubject(examSubject);
        Exam exam = examSubject.getExam();

        return studentSessionService.getStudentsBySessionAndClass(
                exam.getAcademicSession().getId(),
                exam.getClassRoom().getId());

    }

    public Map<Long, BigDecimal> getExistingMarks(ExamSubject examSubject) {

        authorizeExamSubject(examSubject);
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
    @Transactional
    public void saveMarks(Long examSubjectId, List<Long> studentSessionIds, List<String> marks) {

        ExamSubject examSubject = getExamSubject(examSubjectId);
        authorizeExamSubject(examSubject);

        if (studentSessionIds == null || marks == null
                || studentSessionIds.size() != marks.size()) {

            throw new RuntimeException("Marks could not be saved - form data was incomplete.");

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
                throw new RuntimeException("Marks must be numeric.");
            }

            if (marksObtained.signum() < 0
                    || marksObtained.compareTo(BigDecimal.valueOf(examSubject.getMaxMarks())) > 0) {
                throw new RuntimeException("Marks for " + examSubject.getSubject().getSubjectName()
                        + " must be between 0 and " + examSubject.getMaxMarks() + ".");
            }

            StudentSession studentSession = studentSessionRepo.findById(studentSessionId)
                    .orElseThrow(() -> new RuntimeException("Student session not found."));

            Exam exam = examSubject.getExam();
            if (!studentSession.getAcademicSession().getId().equals(exam.getAcademicSession().getId())
                    || !studentSession.getClassRoom().getId().equals(exam.getClassRoom().getId())) {
                throw new RuntimeException("Student does not belong to this exam's academic session/class.");
            }

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
                .orElseThrow(() -> new RuntimeException("Student session not found."));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found."));

        if (!studentSession.getAcademicSession().getId().equals(exam.getAcademicSession().getId())
                || !studentSession.getClassRoom().getId().equals(exam.getClassRoom().getId())) {
            throw new RuntimeException("Student does not belong to this exam's academic session/class.");
        }

        if (currentUserService.hasRole(Role.TEACHER)) {
            var teacher = currentUserService.getCurrentUser().getTeacher();
            if (teacher == null) throw new RuntimeException("Teacher account is not linked to a teacher profile.");
            boolean allowed = teacherSessionRepo.findByTeacherOrderByAcademicSession_IdDesc(teacher)
                    .stream()
                    .anyMatch(ts -> ts.getAcademicSession().getId().equals(exam.getAcademicSession().getId())
                            && ts.getClassRoom().getId().equals(exam.getClassRoom().getId()));
            if (!allowed) throw new RuntimeException("You are not assigned to this class for the selected academic session.");
        }

        return resultRepository.findByStudentSessionAndExamSubject_Exam(
                studentSession,
                exam);

    }

    private void authorizeExamSubject(ExamSubject examSubject) {
        if (!currentUserService.hasRole(Role.TEACHER)) return;
        var user = currentUserService.getCurrentUser();
        if (user.getTeacher() == null) throw new RuntimeException("Teacher account is not linked to a teacher profile.");
        boolean allowed = teacherSessionRepo.findByTeacherOrderByAcademicSession_IdDesc(user.getTeacher())
                .stream()
                .anyMatch(ts -> sameId(ts.getAcademicSession().getId(), examSubject.getExam().getAcademicSession().getId())
                        && sameId(ts.getClassRoom().getId(), examSubject.getExam().getClassRoom().getId())
                        && sameId(ts.getSubject().getId(), examSubject.getSubject().getId()));
        if (!allowed) throw new RuntimeException("You are not assigned to this subject/class for the selected academic session.");
    }

    private boolean sameId(Long left, Long right) {
        return left != null && left.equals(right);
    }

}
