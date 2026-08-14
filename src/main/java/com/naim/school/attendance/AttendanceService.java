package com.naim.school.attendance;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.student.Student;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;
import com.naim.school.security.CurrentUserService;
import com.naim.school.security.Role;
import com.naim.school.teachersession.TeacherSessionRepo;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentSessionRepo studentSessionRepo;
    private final CurrentUserService currentUserService;
    private final TeacherSessionRepo teacherSessionRepo;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentSessionRepo studentSessionRepo,
                             CurrentUserService currentUserService, TeacherSessionRepo teacherSessionRepo) {
        this.attendanceRepository = attendanceRepository;
        this.studentSessionRepo = studentSessionRepo;
        this.currentUserService = currentUserService;
        this.teacherSessionRepo = teacherSessionRepo;
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    public Attendance save(Attendance attendance) {
        if (attendance.getStudent() == null || attendance.getAcademicSession() == null || attendance.getClassroom() == null) {
            throw new RuntimeException("Student, classroom and academic session are required.");
        }
        StudentSession placement = studentSessionRepo
                .findByStudentAndAcademicSession(attendance.getStudent(), attendance.getAcademicSession())
                .orElseThrow(() -> new RuntimeException("Student is not enrolled in the selected academic session."));
        if (!placement.getClassRoom().getId().equals(attendance.getClassroom().getId())) {
            throw new RuntimeException("Selected classroom does not match the student's academic-session placement.");
        }
        if (currentUserService.hasRole(Role.TEACHER)) {
            var user = currentUserService.getCurrentUser();
            if (user.getTeacher() == null) throw new RuntimeException("Teacher account is not linked to a teacher profile.");
            boolean allowed = teacherSessionRepo.findByTeacherOrderByAcademicSession_IdDesc(user.getTeacher())
                    .stream()
                    .anyMatch(ts -> ts.getAcademicSession().getId().equals(attendance.getAcademicSession().getId())
                            && ts.getClassRoom().getId().equals(attendance.getClassroom().getId()));
            if (!allowed) throw new RuntimeException("You are not assigned to this class for the selected academic session.");
        }
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

    public long countByAttendanceDate(LocalDate now) {
       return attendanceRepository.countByAttendanceDate(now);
    }

}