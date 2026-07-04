package com.naim.school.student.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.classroom.entity.ClassRoom;
import com.naim.school.classroom.repository.ClassRoomRepository;
import com.naim.school.student.entity.Student;
import com.naim.school.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final ClassRoomRepository classRoomRepository;

    public List<Student> getAllStudents() {

        return repository.findAll();

    }

    public List<Student> getActiveStudents() {

        return repository.findByActiveTrue();

    }

    public Student getById(Long id) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        if (student.getClassRoom() != null) {

            student.setClassId(student.getClassRoom().getId());

        }

        return student;

    }

    public void save(Student student) {

        // Class Mapping

        if (student.getClassId() != null) {

            ClassRoom classRoom = classRoomRepository
                    .findById(student.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            student.setClassRoom(classRoom);

        }

        // Duplicate Roll Validation

        if (student.getClassRoom() != null
                && repository.existsByRollNoAndClassRoom_Id(
                student.getRollNo(),
                student.getClassRoom().getId())) {

            if (student.getId() == null) {

                throw new RuntimeException("Roll Number already exists.");

            }

        }

        // Save New Student

        if (student.getId() == null) {

            repository.save(student);

            student.setAdmissionNo(

                    String.format("STUD-%04d", student.getId())

            );

        }

        repository.save(student);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }

}