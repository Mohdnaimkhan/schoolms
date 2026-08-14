package com.naim.school.attendance;
import java.util.List;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final ClassRoomService classroomService;
    private final AcademicSessionService academicSessionService;

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        List<Attendance> attendanceList = sessionId == null
                ? attendanceService.findAll()
                : attendanceService.findByAcademicSession(academicSessionService.getById(sessionId));
        long attendancePresent = attendanceList.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long attendanceAbsent = attendanceList.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long attendanceLeave = attendanceList.stream().filter(a -> a.getStatus() == AttendanceStatus.LEAVE).count();

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("attendanceTotal", attendanceList.size());
        model.addAttribute("attendancePresent", attendancePresent);
        model.addAttribute("attendanceAbsent", attendanceAbsent);
        model.addAttribute("attendanceLeave", attendanceLeave);

        return "attendance/list";

    }

    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute("attendance", new Attendance());

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("classrooms", classroomService.getAllClassRooms());

        model.addAttribute("academic", academicSessionService.getAllSessions());

        model.addAttribute("statuses", AttendanceStatus.values());

        return "attendance/form";

    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("attendance") Attendance attendance,
            BindingResult result,
            Model model
    ) {

        if (attendance.getStudent() != null && attendance.getAttendanceDate() != null
                && attendance.getAcademicSession() != null && attendanceService.existsByStudentAndAttendanceDateAndAcademicSession(
                attendance.getStudent(),
                attendance.getAttendanceDate(),
                attendance.getAcademicSession())) {

            result.rejectValue(
                    "attendanceDate",
                    "duplicate",
                    "Attendance already marked for this student."
            );

        }

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            model.addAttribute("classrooms", classroomService.getAllClassRooms());

            model.addAttribute("academic", academicSessionService.getAllSessions());

            model.addAttribute("statuses", AttendanceStatus.values());

            return "attendance/form";

        }

        attendanceService.save(attendance);

        return "redirect:/attendance";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        attendanceService.deleteById(id);

        return "redirect:/attendance";

    }

}
