package com.naim.school.classroom;

import com.naim.school.sms.BusinessException;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository repository;

    /*
     * ==========================================
     * GET ALL
     * ==========================================
     */

    public List<ClassRoom> getAllClassRooms() {

        return repository.findAll();

    }

    /*
     * ==========================================
     * GET BY ID
     * ==========================================
     */

    public ClassRoom getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Class Room not found."));

    }

    /*
     * ==========================================
     * SAVE (ADD + EDIT)
     * ==========================================
     */

    @Transactional
    public void save(ClassRoom classRoom) {

        if (classRoom.getId() == null) {

            if (repository.existsByClassName(classRoom.getClassName())) {

                throw new BusinessException("Class already exists.");

            }

        } else {

            if (repository.existsByClassNameAndIdNot(
                    classRoom.getClassName(),
                    classRoom.getId())) {

                throw new BusinessException("Class already exists.");

            }

        }

        repository.save(classRoom);

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

    @Transactional
    public void changeStatus(Long id) {

        ClassRoom classRoom = getById(id);

        classRoom.setActive(!classRoom.getActive());

        repository.save(classRoom);

    }

}