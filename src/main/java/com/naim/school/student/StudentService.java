package com.naim.school.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naim.school.sms.FileStorageService;
import com.naim.school.sms.NumberGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final FileStorageService fileStorageService;
    private final NumberGenerator numberGenerator;

    /*
     * ==========================================================
     * GET ALL STUDENTS
     * ==========================================================
     */

    public List<Student> getAllStudents() {

        return repository.findAll();

    }

    /*
     * ==========================================================
     * ACTIVE STUDENTS
     * ==========================================================
     */

    public List<Student> getActiveStudents() {

        return repository.findByActiveTrue();

    }

    /*
     * ==========================================================
     * GET BY ID
     * ==========================================================
     */

    public Student getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

    }

    /*
     * ==========================================================
     * SAVE (ADD + EDIT)
     * ==========================================================
     */

    public void save(Student student, MultipartFile photoFile) {

        /*
         * ==========================
         * EDIT MODE
         * ==========================
         */

        if (student.getId() != null) {

            Student oldStudent = getById(student.getId());

            // Keep old photo

            if (photoFile == null || photoFile.isEmpty()) {

                student.setPhoto(oldStudent.getPhoto());

            }

            // Upload new photo

            else {

                if (oldStudent.getPhoto() != null) {

                    fileStorageService.delete(
                            "students",
                            oldStudent.getPhoto());

                }

                student.setPhoto(

                        fileStorageService.uploadStudentPhoto(photoFile)

                );

            }

        }

        /*
         * ==========================
         * NEW STUDENT
         * ==========================
         */

        else {

            // Upload Photo

            if (photoFile != null && !photoFile.isEmpty()) {

                student.setPhoto(

                        fileStorageService.uploadStudentPhoto(photoFile)

                );

            }

            // Admission Number

            student.setAdmissionNo(

                    numberGenerator.generateAdmissionNo()

            );

            // Roll Number

            student.setRollNumber(

                    numberGenerator.generateRollNo(

                            student.getAcademicSession().getId(),

                            student.getClassRoom().getId()

                    )

            );

        }

        repository.save(student);

    }

    /*
     * ==========================================================
     * DELETE
     * ==========================================================
     */

    public void delete(Long id) {

        Student student = getById(id);

        if (student.getPhoto() != null) {

            fileStorageService.delete(
                    "students",
                    student.getPhoto());

        }

        repository.delete(student);

    }

    /*
     * ==========================================================
     * DASHBOARD
     * ==========================================================
     */

    public long count() {

        return repository.count();

    }

    public List<Student> findTop5ByOrderByIdDesc() {

        return repository.findTop5ByOrderByIdDesc();

    }

    public long countByAdmissionDateBetween(
            LocalDate firstDay,
            LocalDate lastDay) {

        return repository.countByAdmissionDateBetween(
                firstDay,
                lastDay);

    }

    public long countActiveStudents() {

        return repository.countByActiveTrue();

    }

    public long countBoys() {

        return repository.countByGender(Gender.MALE);

    }

    public long countGirls() {

        return repository.countByGender(Gender.FEMALE);

    }
}