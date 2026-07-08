package com.naim.school.attendance;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.student.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void deleteById(Long id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> findByAttendanceDate(LocalDate attendanceDate) {
        return attendanceRepository.findByAttendanceDate(attendanceDate);
    }

    public List<Attendance> findByClassroom(ClassRoom classroom) {
        return attendanceRepository.findByClassroom(classroom);
    }

    public List<Attendance> findByAcademicSession(AcademicSession academicSession) {
        return attendanceRepository.findByAcademicSession(academicSession);
    }

    public List<Attendance> findByStudent(Student student) {
        return attendanceRepository.findByStudent(student);
    }

    public List<Attendance> findByClassroomAndAttendanceDate(
            ClassRoom classroom,
            LocalDate attendanceDate
    ) {
        return attendanceRepository.findByClassroomAndAttendanceDate(
                classroom,
                attendanceDate
        );
    }

    public Optional<Attendance> findByStudentAndAttendanceDateAndAcademicSession(
            Student student,
            LocalDate attendanceDate,
            AcademicSession academicSession
    ) {
        return attendanceRepository.findByStudentAndAttendanceDateAndAcademicSession(
                student,
                attendanceDate,
                academicSession
        );
    }

    public boolean existsByStudentAndAttendanceDateAndAcademicSession(
            Student student,
            LocalDate attendanceDate,
            AcademicSession academicSession
    ) {
        return attendanceRepository.existsByStudentAndAttendanceDateAndAcademicSession(
                student,
                attendanceDate,
                academicSession
        );
    }

    public List<Attendance> findTop5ByOrderByIdDesc() {
      
      return attendanceRepository.findTop5ByOrderByIdDesc();
    }

}