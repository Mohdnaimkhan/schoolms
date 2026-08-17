package com.naim.school.teachersession;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.teacher.Teacher;

public interface TeacherSessionRepo extends JpaRepository<TeacherSession, Long> {

    // Current Session of Teacher
    Optional<TeacherSession> findByTeacherAndCurrentSessionTrue(Teacher teacher);

    // All Session History
    List<TeacherSession> findByTeacherOrderByAcademicSession_IdDesc(Teacher teacher);

    // Session Wise Assignments
    List<TeacherSession> findByAcademicSession(AcademicSession academicSession);

    // Current Assignments
    List<TeacherSession> findByCurrentSessionTrue();

}
