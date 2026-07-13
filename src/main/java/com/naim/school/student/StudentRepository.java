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

        long countByAdmissionDateBetween(LocalDate firstDay,
                        LocalDate lastDay);

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

        long countByActiveTrue();

        long countByGender(Gender male);

}