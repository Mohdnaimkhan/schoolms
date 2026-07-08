package com.naim.school.attendance;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    List<Attendance> findByClassroom(ClassRoom classroom);

    List<Attendance> findByAcademicSession(AcademicSession academicSession);

    List<Attendance> findByStudent(Student student);

    List<Attendance> findByClassroomAndAttendanceDate(
            ClassRoom classroom,
            LocalDate attendanceDate
    );

    Optional<Attendance> findByStudentAndAttendanceDateAndAcademicSession(
            Student student,
            LocalDate attendanceDate,
            AcademicSession academicSession
    );

    boolean existsByStudentAndAttendanceDateAndAcademicSession(
            Student student,
            LocalDate attendanceDate,
            AcademicSession academicSession
    );

    List<Attendance> findTop5ByOrderByIdDesc();

}