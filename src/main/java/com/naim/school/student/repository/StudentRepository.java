package com.naim.school.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByActiveTrue();

    boolean existsByRollNoAndClassRoom_Id(Integer rollNo, Long classId);

}