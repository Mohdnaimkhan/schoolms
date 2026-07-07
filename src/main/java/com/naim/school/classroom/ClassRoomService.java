package com.naim.school.classroom;

import java.util.List;


import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository repository;

    /**
     * Get All Classes
     */
    public List<ClassRoom> getAllClasses() {

        return repository.findAll();

    }

    /**
     * Get Active Classes
     */
    public List<ClassRoom> getActiveClasses() {

        return repository.findByActiveTrue();

    }

    /**
     * Get Class By Id
     */
    public ClassRoom getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found."));

    }

    /**
     * Save Or Update
     */
    public void save(ClassRoom classRoom) {

        // New Record
        if (classRoom.getId() == null) {

            if (repository.existsByClassNameAndSection(
                    classRoom.getClassName(),
                    classRoom.getSection())) {

                throw new RuntimeException("Class already exists.");

            }

            // Generate Class Code Automatically
            if (classRoom.getClassCode() == null
                    || classRoom.getClassCode().isBlank()) {

                classRoom.setClassCode(

                        classRoom.getClassName().trim().toUpperCase()

                                + "-"

                                + classRoom.getSection().trim().toUpperCase()

                );

            }

        }

        // Save / Update
        repository.save(classRoom);

    }

    /**
     * Delete
     */
    public void delete(Long id) {

        repository.deleteById(id);

    }

  
}