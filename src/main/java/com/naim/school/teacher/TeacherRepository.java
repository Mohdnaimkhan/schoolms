package com.naim.school.teacher;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByActiveTrue();

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

}