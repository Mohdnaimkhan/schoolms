package com.naim.school.result;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.exam.Exam;
import com.naim.school.exam.ExamSubject;
import com.naim.school.studentsession.StudentSession;

public interface ResultRepository extends JpaRepository<Result, Long> {

    // Existing mark for a student in a specific exam-subject (used to avoid duplicates)
    Optional<Result> findByStudentSessionAndExamSubject(StudentSession studentSession, ExamSubject examSubject);

    // All marks for a student across every subject of one exam (report card)
    List<Result> findByStudentSessionAndExamSubject_Exam(StudentSession studentSession, Exam exam);

    // All marks entered for one exam-subject (marks entry grid)
    List<Result> findByExamSubject(ExamSubject examSubject);

    // All results for one student-session (used by Student Profile)
    List<Result> findByStudentSession(StudentSession studentSession);

}
