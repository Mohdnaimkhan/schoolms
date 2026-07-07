package com.naim.school.subject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByActiveTrue();

    boolean existsBySubjectNameAndClassRoom_Id(
            String subjectName,
            Long classId);

}