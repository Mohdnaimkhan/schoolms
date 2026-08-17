package com.naim.school.exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Long> {

    List<ExamSubject> findByExam(Exam exam);

}
