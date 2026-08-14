package com.naim.school.classroom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    boolean existsByClassName(String className);

    boolean existsByClassNameAndIdNot(String className, Long id);

}