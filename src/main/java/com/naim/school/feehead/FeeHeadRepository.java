package com.naim.school.feehead;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeHeadRepository extends JpaRepository<FeeHead, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
