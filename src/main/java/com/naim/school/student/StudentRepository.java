package com.naim.school.student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    /*
     * ==========================================================
     * BASIC
     * ==========================================================
     */

    List<Student> findByActiveTrue();

    List<Student> findTop5ByOrderByIdDesc();

    long countByAdmissionDateBetween(
            LocalDate firstDay,
            LocalDate lastDay);

    long countByActiveTrue();

    long countByGender(Gender gender);

    /*
     * ==========================================================
     * ADMISSION NUMBER
     * ==========================================================
     */

    Optional<Student> findTopByOrderByIdDesc();

    boolean existsByAdmissionNo(String admissionNo);

    /*
     * ==========================================================
     * ROLL NUMBER
     * ==========================================================
     */

    Optional<Student> findTopByAcademicSession_IdAndClassRoom_IdOrderByRollNumberDesc(
            Long academicSessionId,
            Long classRoomId);

    boolean existsByRollNumberAndAcademicSession_IdAndClassRoom_Id(
            String rollNumber,
            Long academicSessionId,
            Long classRoomId);

    /*
     * ==========================================================
     * DUPLICATE CHECK
     * ==========================================================
     */

    boolean existsByMobile(String mobile);

    boolean existsByMobileAndIdNot(
            String mobile,
            Long id);

    boolean existsByAadharNumber(String aadharNumber);

    boolean existsByAadharNumberAndIdNot(
            String aadharNumber,
            Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(
            String email,
            Long id);

    /*
     * ==========================================================
     * SEARCH
     * ==========================================================
     */

    List<Student> findByFullNameContainingIgnoreCase(String fullName);

    List<Student> findByClassRoom_Id(Long classRoomId);

    List<Student> findByAcademicSession_Id(Long academicSessionId);

    List<Student> findByAcademicSession_IdAndClassRoom_Id(
            Long academicSessionId,
            Long classRoomId);

    List<Student> findByAcademicSession_IdAndClassRoom_IdAndActiveTrue(
            Long academicSessionId,
            Long classRoomId);

}