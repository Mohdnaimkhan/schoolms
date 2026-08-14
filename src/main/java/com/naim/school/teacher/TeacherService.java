package com.naim.school.teacher;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naim.school.sms.Constants;
import com.naim.school.sms.FileStorageService;


import org.springframework.stereotype.Service;

import com.naim.school.subject.SubjectRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository repository;
    private final SubjectRepository subjectRepository;
    private final FileStorageService fileStorageService;

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

    public void save(Teacher teacher, MultipartFile photoFile) {

        teacher.setSubject(teacher.getSubjectId() == null
                ? null
                : subjectRepository.findById(teacher.getSubjectId())
                        .orElseThrow(() -> new RuntimeException("Subject not found.")));

        if (teacher.getId() != null) {
            Teacher oldTeacher = getById(teacher.getId());

            if (photoFile == null || photoFile.isEmpty()) {
                teacher.setPhoto(oldTeacher.getPhoto());
            } else {
                String newPhoto = fileStorageService.uploadTeacherPhoto(photoFile);
                teacher.setPhoto(newPhoto);
                repository.save(teacher);
                if (oldTeacher.getPhoto() != null && !oldTeacher.getPhoto().equals(newPhoto)) {
                    fileStorageService.delete(Constants.TEACHER_FOLDER, oldTeacher.getPhoto());
                }
                return;
            }
        } else if (photoFile != null && !photoFile.isEmpty()) {
            teacher.setPhoto(fileStorageService.uploadTeacherPhoto(photoFile));
        }

        if (teacher.getId() == null) {
            repository.save(teacher);
            teacher.setEmployeeCode(String.format("TEACH-%04d", teacher.getId()));
        }

        repository.save(teacher);

    }

  

    public void delete(Long id) {

        Teacher teacher = getById(id);
        repository.delete(teacher);

        if (teacher.getPhoto() != null) {
            fileStorageService.delete(Constants.TEACHER_FOLDER, teacher.getPhoto());
        }

    }

    public long count() {
       return repository.count();
    }

}
