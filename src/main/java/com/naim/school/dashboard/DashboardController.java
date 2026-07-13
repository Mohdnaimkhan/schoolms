package com.naim.school.dashboard;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.attendance.Attendance;
import com.naim.school.attendance.AttendanceService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.fee.Fee;
import com.naim.school.fee.FeeService;
import com.naim.school.fee.FeeStatus;
import com.naim.school.student.Student;
import com.naim.school.student.StudentService;
import com.naim.school.subject.SubjectService;
import com.naim.school.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

        private final StudentService studentService;

        private final TeacherService teacherService;

        private final ClassRoomService classRoomService;

        private final SubjectService subjectService;

        private final FeeService feeService;

        private final AttendanceService attendanceService;

        private final AcademicSessionService academicSessionService;

        @GetMapping("/")
        public String dashboard(Model model) {

                /*
                 * ===========================
                 * COUNTS
                 * ===========================
                 */

                model.addAttribute("studentCount",
                                studentService.getActiveStudents().size());

                model.addAttribute("teacherCount",
                                teacherService.getActiveTeachers().size());

                model.addAttribute("classRoomCount",
                                classRoomService.getActiveClasses().size());

                model.addAttribute("subjectCount",
                                subjectService.getAllSubjects().size());

                /*
                 * ===========================
                 * Today
                 * ===========================
                 */

                model.addAttribute(
                                "todayAttendance",
                                attendanceService.countByAttendanceDate(LocalDate.now()));

                BigDecimal todayCollection = feeService.findByPaymentDate(LocalDate.now())
                                .stream()
                                .map(Fee::getPaidAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                model.addAttribute("todayCollection", todayCollection);
                model.addAttribute(
                                "pendingFees",
                                feeService.countByStatus(FeeStatus.PENDING));
                LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
                LocalDate lastDay = LocalDate.now();

                model.addAttribute(
                                "newAdmissions",
                                studentService.countByAdmissionDateBetween(firstDay, lastDay));
                /*
                 * ===========================
                 * CURRENT SESSION
                 * ===========================
                 */

                AcademicSession currentSession = academicSessionService.findCurrentSession().orElse(null);

                model.addAttribute("currentSession", currentSession);

                /*
                 * ===========================
                 * RECENT STUDENTS
                 * ===========================
                 */

                List<Student> recentStudents = studentService.findTop5ByOrderByIdDesc();

                model.addAttribute("recentStudents", recentStudents);

                /*
                 * ===========================
                 * RECENT FEES
                 * ===========================
                 */

                List<Fee> recentFees = feeService.findTop5ByOrderByIdDesc();

                model.addAttribute("recentFees", recentFees);

                /*
                 * ===========================
                 * RECENT ATTENDANCE
                 * ===========================
                 */

                List<Attendance> recentAttendance = attendanceService.findTop5ByOrderByIdDesc();

                model.addAttribute("recentAttendance",
                                recentAttendance);

                return "dashboard/index";

        }

}