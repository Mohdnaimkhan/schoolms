package com.naim.school.teacher.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.subject.repository.SubjectRepository;
import com.naim.school.teacher.entity.Teacher;
import com.naim.school.teacher.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository repository;
    private final SubjectRepository subjectRepository;

    public List<Teacher> getAllTeachers() {

        return repository.findAll();

    }

    public List<Teacher> getActiveTeachers() {

        return repository.findByActiveTrue();

    }

   public Teacher getById(Long id){

    Teacher teacher = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher Not Found"));

    if(teacher.getSubject()!=null){

        teacher.setSubjectId(
                teacher.getSubject().getId());

    }

    return teacher;

}

    public void save(Teacher teacher) {

        if (teacher.getSubjectId() != null) {

            teacher.setSubject(
                    subjectRepository.findById(teacher.getSubjectId())
                            .orElse(null));
        }

        if (teacher.getId() == null) {

            repository.save(teacher);

            teacher.setEmployeeCode(
                    String.format("TEACH-%04d", teacher.getId()));

        }

        repository.save(teacher);

    }

  

    public void delete(Long id) {

        repository.deleteById(id);

    }

}