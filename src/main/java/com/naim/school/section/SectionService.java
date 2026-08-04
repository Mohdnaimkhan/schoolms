package com.naim.school.section;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository repository;

    /*
     * ==========================================
     * GET ALL
     * ==========================================
     */

    public List<Section> getAllSections() {

        return repository.findAll();

    }

    /*
     * ==========================================
     * GET BY ID
     * ==========================================
     */

    public Section getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found."));

    }

    /*
     * ==========================================
     * SAVE (ADD + EDIT)
     * ==========================================
     */

    @Transactional
    public void save(Section section) {

        // Duplicate Validation

        if (section.getId() == null) {

            if (repository.existsBySectionName(section.getSectionName())) {

                throw new RuntimeException("Section already exists.");

            }

        } else {

            if (repository.existsBySectionNameAndIdNot(
                    section.getSectionName(),
                    section.getId())) {

                throw new RuntimeException("Section already exists.");

            }

        }

        repository.save(section);

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

    @Transactional
    public void changeStatus(Long id) {

        Section section = getById(id);

        section.setActive(!section.getActive());

        repository.save(section);

    }

}