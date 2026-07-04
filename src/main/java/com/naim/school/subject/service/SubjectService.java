package com.naim.school.subject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.subject.entity.Subject;
import com.naim.school.subject.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository repository;

    public List<Subject> getAllSubjects() {

        return repository.findAll();

    }

    public Subject getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found."));

    }

    public void save(Subject subject) {

        if (subject.getId() == null) {

            if (repository.existsBySubjectNameAndClassRoom_Id(
                    subject.getSubjectName(),
                    subject.getClassRoom().getId())) {

                throw new RuntimeException("Subject already exists.");

            }

            if (subject.getSubjectCode() == null
                    || subject.getSubjectCode().isBlank()) {

                subject.setSubjectCode(

                        subject.getSubjectName()
                                .substring(0, 3)
                                .toUpperCase()

                );

            }

        }

        repository.save(subject);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }

}