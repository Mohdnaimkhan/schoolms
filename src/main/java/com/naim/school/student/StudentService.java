package com.naim.school.student;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.naim.school.sms.Constants;
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
     * GET BY ID
     * ==========================================================
     */

    public Student getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found."));

    }

    /*
     * ==========================================================
     * SAVE (ADD + EDIT)
     * ==========================================================
     */

    @Transactional
    public void save(Student student, MultipartFile photoFile) {

        normalize(student);

        /*
         * ==========================================================
         * DUPLICATE AADHAAR
         * ==========================================================
         */

        if (existsAadhaar(
                student.getAadhaarNumber(),
                student.getId())) {

            throw new IllegalArgumentException(
                    "Aadhaar Number already exists.");

        }

        /*
         * ==========================================================
         * EDIT
         * ==========================================================
         */

        if (student.getId() != null) {

            Student oldStudent = getById(student.getId());

            /*
             * Admission No Never Change
             */

            student.setAdmissionNo(
                    oldStudent.getAdmissionNo());

            /*
             * Keep Old Photo
             */

            if (photoFile == null ||
                    photoFile.isEmpty()) {

                student.setPhoto(
                        oldStudent.getPhoto());

            }

            /*
             * Upload New Photo
             */

            else {

                if (oldStudent.getPhoto() != null) {

                    fileStorageService.delete(
                            Constants.STUDENT_FOLDER,
                            oldStudent.getPhoto());

                }

                student.setPhoto(
                        fileStorageService.uploadStudentPhoto(photoFile));

            }

        }

        /*
         * ==========================================================
         * NEW STUDENT
         * ==========================================================
         */

        else {

            student.setAdmissionNo(
                    numberGenerator.generateAdmissionNo());

            if (photoFile != null &&
                    !photoFile.isEmpty()) {

                student.setPhoto(
                        fileStorageService.uploadStudentPhoto(photoFile));

            }

        }

        repository.save(student);

    }

    /*
     * ==========================================================
     * CHANGE STATUS
     * ==========================================================
     */

    @Transactional
    public void changeStatus(
            Long id,
            StudentStatus status) {

        Student student = getById(id);

        student.setStatus(status);

        repository.save(student);

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

    public long countActiveStudents() {

        return repository.countByStatus(
                StudentStatus.ACTIVE);

    }

    public long countBoys() {

        return repository.countByGender(
                Gender.MALE);

    }

    public long countGirls() {

        return repository.countByGender(
                Gender.FEMALE);

    }

    /*
     * ==========================================================
     * SEARCH
     * ==========================================================
     */

    public List<Student> searchByStudentName(
            String keyword) {

        return repository.findByStudentNameContainingIgnoreCase(keyword);

    }

    public List<Student> searchByFatherName(
            String keyword) {

        return repository.findByFatherNameContainingIgnoreCase(keyword);

    }

    public List<Student> searchByAdmissionNo(
            String keyword) {

        return repository.findByAdmissionNoContainingIgnoreCase(keyword);

    }

    /*
     * ==========================================================
     * DUPLICATE AADHAAR
     * ==========================================================
     */

    public boolean existsAadhaar(
            String aadhaarNumber,
            Long id) {

        if (aadhaarNumber == null ||
                aadhaarNumber.isBlank()) {

            return false;

        }

        aadhaarNumber =
                aadhaarNumber.replaceAll("\\s+", "");

        if (id == null) {

            return repository.existsByAadhaarNumber(
                    aadhaarNumber);

        }

        return repository.existsByAadhaarNumberAndIdNot(
                aadhaarNumber,
                id);

    }

    /*
     * ==========================================================
     * NORMALIZE
     * ==========================================================
     */

    private void normalize(Student student) {

        if (student.getStudentName() != null) {

            student.setStudentName(
                    student.getStudentName().trim());

        }

        if (student.getFatherName() != null) {

            student.setFatherName(
                    student.getFatherName().trim());

        }

        if (student.getMotherName() != null) {

            student.setMotherName(
                    student.getMotherName().trim());

        }

        if (student.getGuardianName() != null) {

            student.setGuardianName(
                    student.getGuardianName().trim());

        }

        if (student.getGuardianRelation() != null) {

            student.setGuardianRelation(
                    student.getGuardianRelation().trim());

        }

        if (student.getMobileNumber() != null) {

            String mobile = student.getMobileNumber()
                    .replaceAll("\\s+", "");

            student.setMobileNumber(
                    mobile.isBlank()
                            ? null
                            : mobile);

        }

        if (student.getEmergencyContact() != null) {

            String emergency = student.getEmergencyContact()
                    .replaceAll("\\s+", "");

            student.setEmergencyContact(
                    emergency.isBlank()
                            ? null
                            : emergency);

        }

        if (student.getAadhaarNumber() != null) {

            String aadhaar = student.getAadhaarNumber()
                    .replaceAll("\\s+", "");

            student.setAadhaarNumber(
                    aadhaar.isBlank()
                            ? null
                            : aadhaar);

        }

        if (student.getEmail() != null) {

            student.setEmail(
                    student.getEmail()
                            .trim()
                            .toLowerCase());

        }

        if (student.getAddress() != null) {

            student.setAddress(
                    student.getAddress().trim());

        }

        if (student.getRemarks() != null) {

            student.setRemarks(
                    student.getRemarks().trim());

        }

    }

}