package com.naim.school.classroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    Optional<ClassRoom> findByClassCode(String classCode);

    List<ClassRoom> findByActiveTrue();

    List<ClassRoom> findByClassNameContainingIgnoreCase(String keyword);

    boolean existsByClassNameAndSection(String className, String section);

}