package com.naim.school.schools;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchoolService {

    private final SchoolRepository repository;

    public SchoolService(SchoolRepository repository) {
        this.repository = repository;
    }

    public Optional<School> getSchool() {
        return repository.findFirstByOrderByIdAsc();
    }

    public School save(School school) {
        return repository.save(school);
    }

    public boolean schoolExists() {
        return repository.count() > 0;
    }

    public boolean existsBySchoolCode(String schoolCode) {
        return repository.existsBySchoolCode(schoolCode);
    }

}