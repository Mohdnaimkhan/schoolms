package com.naim.school.subject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.subject.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByActiveTrue();

    boolean existsBySubjectNameAndClassRoom_Id(
            String subjectName,
            Long classId);

}