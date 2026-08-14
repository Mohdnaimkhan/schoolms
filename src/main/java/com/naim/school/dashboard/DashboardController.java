package com.naim.school.dashboard;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.attendance.Attendance;
import com.naim.school.attendance.AttendanceService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.fee.Fee;
import com.naim.school.fee.FeeService;
import com.naim.school.fee.FeeStatus;
import com.naim.school.notice.Notice;
import com.naim.school.notice.NoticeService;
import com.naim.school.sms.AppInfo;
import com.naim.school.student.Student;
import com.naim.school.student.StudentService;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionService;
import com.naim.school.subject.SubjectService;
import com.naim.school.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        private final StudentSessionService studentSessionService;

        private final NoticeService noticeService;

        @GetMapping("/")
        public String dashboard(Model model) {

                /*
                 * ===========================
                 * COUNTS
                 * ===========================
                 */

                model.addAttribute("studentCount",
                                studentService.count());

                model.addAttribute("teacherCount",
                                teacherService.getActiveTeachers().size());

                model.addAttribute("classRoomCount",
                                classRoomService.getAllClassRooms().size());

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

                AcademicSession currentSession = academicSessionService.getCurrentSessionOrNull();

                model.addAttribute("currentSession", currentSession);

                /*
                 * ===========================
                 * RECENT STUDENTS
                 * ===========================
                 */

                List<Student> recentStudents = studentService.findTop5ByOrderByIdDesc();

                model.addAttribute("recentStudents", recentStudents);

                Map<Long, String> currentClassByStudent = new HashMap<>();

                for (Student student : recentStudents) {

                        StudentSession session = studentSessionService.getCurrentSession(student);

                        currentClassByStudent.put(
                                        student.getId(),
                                        (session != null && session.getClassRoom() != null)
                                                        ? session.getClassRoom().getClassName()
                                                        : "-");

                }

                model.addAttribute("currentClassByStudent", currentClassByStudent);

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

                /*
                 * ===========================
                 * RECENT NOTICES
                 * ===========================
                 */

                List<Notice> recentNotices = noticeService.getActive()
                                .stream()
                                .limit(5)
                                .toList();

                model.addAttribute("recentNotices", recentNotices);

                return "dashboard/index";

        }
        /*
         * =========================================
         * About
         * =========================================
         */

        @GetMapping("/about")
        public String about(Model model) {

                model.addAttribute("appName", AppInfo.APP_NAME);
                model.addAttribute("version", AppInfo.VERSION);
                model.addAttribute("releaseDate", AppInfo.RELEASE_DATE);
                model.addAttribute("lastUpdated", AppInfo.LAST_UPDATED);
                model.addAttribute("developer", AppInfo.DEVELOPER);

                return "about/about";
        }
}