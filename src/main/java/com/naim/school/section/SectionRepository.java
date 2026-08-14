package com.naim.school.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsBySectionName(String sectionName);

    boolean existsBySectionNameAndIdNot(String sectionName, Long id);

}