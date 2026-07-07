package com.naim.school.attendance;

import com.naim.school.classroom.ClassRoomService;
import com.naim.school.session.AcademicSessionService;
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
    public String list(Model model) {

        model.addAttribute("attendanceList", attendanceService.findAll());

        return "attendance/list";

    }

    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute("attendance", new Attendance());

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("classrooms", classroomService.getAllClasses());

        model.addAttribute("sessions", academicSessionService.getAllSessions());

        model.addAttribute("statuses", AttendanceStatus.values());

        return "attendance/form";

    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("attendance") Attendance attendance,
            BindingResult result,
            Model model
    ) {

        if (attendanceService.existsByStudentAndAttendanceDateAndAcademicSession(
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

            model.addAttribute("classrooms", classroomService.getAllClasses());

            model.addAttribute("sessions", academicSessionService.getAllSessions());

            model.addAttribute("statuses", AttendanceStatus.values());

            return "attendance/form";

        }

        attendanceService.save(attendance);

        return "redirect:/attendance";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        attendanceService.deleteById(id);

        return "redirect:/attendance";

    }

}