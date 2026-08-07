package com.naim.school.student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    /*
     * ==========================================================
     * DASHBOARD
     * ==========================================================
     */

    List<Student> findTop5ByOrderByIdDesc();

    long countByStatus(StudentStatus status);

    long countByGender(Gender gender);

    /*
     * ==========================================================
     * ADMISSION NUMBER
     * ==========================================================
     */

    Optional<Student> findTopByOrderByIdDesc();

    Optional<Student> findByAdmissionNo(String admissionNo);

    boolean existsByAdmissionNo(String admissionNo);

    boolean existsByAdmissionNoAndIdNot(
            String admissionNo,
            Long id);

    /*
     * ==========================================================
     * AADHAAR DUPLICATE CHECK
     * ==========================================================
     */

    boolean existsByAadhaarNumber(String aadhaarNumber);

    boolean existsByAadhaarNumberAndIdNot(
            String aadhaarNumber,
            Long id);

    /*
     * ==========================================================
     * SEARCH
     * ==========================================================
     */

    List<Student> findByStudentNameContainingIgnoreCase(
            String studentName);

    List<Student> findByFatherNameContainingIgnoreCase(
            String fatherName);

    List<Student> findByAdmissionNoContainingIgnoreCase(
            String admissionNo);

    /*
     * ==========================================================
     * FILTER
     * ==========================================================
     */

    List<Student> findByStatus(StudentStatus status);

    List<Student> findByGender(Gender gender);

}