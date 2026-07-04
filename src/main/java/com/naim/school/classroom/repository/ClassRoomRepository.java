package com.naim.school.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.classroom.entity.ClassRoom;


public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    Optional<ClassRoom> findByClassCode(String classCode);

    List<ClassRoom> findByActiveTrue();

    List<ClassRoom> findByClassNameContainingIgnoreCase(String keyword);

    boolean existsByClassNameAndSection(String className, String section);

}