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
<<<<<<< HEAD
    List<StudentSession> findByStudentOrderByAcademicSession_IdDesc(Student student);

    Optional<StudentSession> findByStudentAndAcademicSession(Student student, AcademicSession academicSession);

    // Session + Class Wise Students
    List<StudentSession> findByAcademicSession_IdAndClassRoom_Id(Long sessionId,
=======
    List<StudentSession> findByStudentOrderByAcademicSessionIdDesc(Student student);

    // Session + Class Wise Students
    List<StudentSession> findByAcademicSessionIdAndClassRoomId(Long sessionId,
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
                                                               Long classRoomId);

    // Session Wise Students
    List<StudentSession> findByAcademicSession(AcademicSession academicSession);

    // Current Students
    List<StudentSession> findByCurrentSessionTrue();

    // Current Students of Session
<<<<<<< HEAD
    List<StudentSession> findByAcademicSession_IdAndCurrentSessionTrue(Long sessionId);
=======
    List<StudentSession> findByAcademicSessionIdAndCurrentSessionTrue(Long sessionId);
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

}