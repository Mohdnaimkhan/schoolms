package com.naim.school.schools;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findFirstByOrderByIdAsc();

    boolean existsBySchoolCode(String schoolCode);

}
