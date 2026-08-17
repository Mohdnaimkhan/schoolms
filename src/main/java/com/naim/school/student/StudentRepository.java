package com.naim.school.student;

import java.time.LocalDate;
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

    long countByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);

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

    boolean existsByPenNo(String penNo);

    boolean existsByPenNoAndIdNot(String penNo, Long id);

    boolean existsByApaarId(String apaarId);

    boolean existsByApaarIdAndIdNot(String apaarId, Long id);

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

    @org.springframework.data.jpa.repository.Query("""
        SELECT s FROM Student s
        WHERE (:keyword IS NULL OR :keyword = ''
               OR LOWER(COALESCE(s.studentName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.admissionNo, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.fatherName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.mobileNumber, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.emergencyContact, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.aadhaarNumber, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.penNo, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(s.apaarId, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:status IS NULL OR s.status = :status)
        ORDER BY s.id DESC
        """)
    List<Student> search(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("status") StudentStatus status);

    /*
     * ==========================================================
     * FILTER
     * ==========================================================
     */

    List<Student> findByStatus(StudentStatus status);

    List<Student> findByGender(Gender gender);

}