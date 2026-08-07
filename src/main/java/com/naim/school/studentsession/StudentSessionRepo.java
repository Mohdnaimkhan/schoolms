package com.naim.school.studentsession;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.student.Student;

public interface StudentSessionRepo extends JpaRepository<StudentSession, Long> {

    // Current Session of Student
    Optional<StudentSession> findByStudentAndCurrentSessionTrue(Student student);

    // All Session History
    List<StudentSession> findByStudentOrderByAcademicSessionIdDesc(Student student);

    // Session + Class Wise Students
    List<StudentSession> findByAcademicSessionIdAndClassRoomId(Long sessionId,
                                                               Long classRoomId);

    // Session Wise Students
    List<StudentSession> findByAcademicSession(AcademicSession academicSession);

    // Current Students
    List<StudentSession> findByCurrentSessionTrue();

    // Current Students of Session
    List<StudentSession> findByAcademicSessionIdAndCurrentSessionTrue(Long sessionId);

}