package com.naim.school.teacher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.teacher.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByActiveTrue();

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

}