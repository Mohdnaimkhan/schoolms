package com.naim.school.exam;

import com.naim.school.sms.BusinessException;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.result.ResultRepository;
import com.naim.school.subject.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final ResultRepository resultRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;

    /*
     * ==========================================================
     * EXAM - SAVE / EDIT
     * ==========================================================
     */

    public Exam save(Exam exam) {

        if (exam.getAcademicSessionId() == null) {

            throw new BusinessException("Academic session is required.");

        }

        exam.setAcademicSession(
                academicSessionRepository.findById(exam.getAcademicSessionId())
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

        if (exam.getClassRoomId() == null) {

            throw new BusinessException("Class is required.");

        }

        exam.setClassRoom(
                classRoomRepository.findById(exam.getClassRoomId())
                        .orElseThrow(() -> new BusinessException("Class not found.")));

        return examRepository.save(exam);

    }

    public Exam getById(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Exam not found."));

        if (exam.getAcademicSession() != null) {

            exam.setAcademicSessionId(exam.getAcademicSession().getId());

        }

        if (exam.getClassRoom() != null) {

            exam.setClassRoomId(exam.getClassRoom().getId());

        }

        return exam;

    }

    public List<Exam> getAll() {

        return examRepository.findAll();

    }

    public List<Exam> getByAcademicSession(Long sessionId) {

        return examRepository.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

    }

    public void delete(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Exam not found."));

        List<ExamSubject> examSubjects = examSubjectRepository.findByExam(exam);

        for (ExamSubject examSubject : examSubjects) {

            resultRepository.deleteAll(
                    resultRepository.findByExamSubject(examSubject));

            examSubjectRepository.delete(examSubject);

        }

        examRepository.delete(exam);

    }

    /*
     * ==========================================================
     * EXAM SUBJECTS
     * ==========================================================
     */

    public List<ExamSubject> getSubjects(Long examId) {

        Exam exam = getById(examId);

        List<ExamSubject> subjects = examSubjectRepository.findByExam(exam);

        subjects.sort(Comparator.comparing(
                es -> es.getSubject() != null ? es.getSubject().getSubjectName() : ""));

        return subjects;

    }

    public void addSubject(Long examId, Long subjectId, Integer maxMarks, Integer passMarks) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new BusinessException("Exam not found."));

        ExamSubject examSubject = new ExamSubject();

        examSubject.setExam(exam);

        examSubject.setSubject(
                subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new BusinessException("Subject not found.")));

        examSubject.setMaxMarks(maxMarks == null ? 100 : maxMarks);
        examSubject.setPassMarks(passMarks == null ? 33 : passMarks);

        examSubjectRepository.save(examSubject);

    }

    public void deleteSubject(Long examSubjectId) {

        ExamSubject examSubject = examSubjectRepository.findById(examSubjectId)
                .orElseThrow(() -> new BusinessException("Exam subject not found."));

        resultRepository.deleteAll(
                resultRepository.findByExamSubject(examSubject));

        examSubjectRepository.delete(examSubject);

    }

    public ExamSubject getExamSubjectById(Long id) {

        return examSubjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Exam subject not found."));

    }

}
