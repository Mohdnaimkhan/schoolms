package com.naim.school.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByActiveTrue();

    boolean existsByRollNoAndClassRoom_Id(Integer rollNo, Long classId);

}