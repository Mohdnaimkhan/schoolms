package com.naim.school.studentsession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.student.StudentService;
import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.section.SectionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student-sessions")
public class StudentSessionController {

    private final StudentSessionService studentSessionService;

    private final StudentService studentService;

    private final AcademicSessionService academicSessionService;

    private final ClassRoomService classRoomService;

    private final SectionService sectionService;

    // ===========================
    // List
    // ===========================

    @GetMapping
    public String list(Model model) {

        model.addAttribute("studentSessions",
                studentSessionService.getAll());

        return "studentsession/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("studentSession",
                new StudentSession());

        loadMasters(model);

        return "studentsession/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("studentSession",
                studentSessionService.getById(id));

        loadMasters(model);

        return "studentsession/form";

    }

    // ===========================
    // Save
    // ===========================

    @PostMapping("/save")
    public String save(@ModelAttribute StudentSession studentSession) {

        studentSessionService.save(studentSession);

        return "redirect:/student-sessions";

    }

    // ===========================
    // Delete (Optional)
    // ===========================

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        studentSessionService.delete(id);

        return "redirect:/student-sessions";

    }

    // ===========================
    // Common Master Data
    // ===========================

    private void loadMasters(Model model) {

        model.addAttribute("students",
                studentService.countActiveStudents());

        model.addAttribute("academicSessions",
                academicSessionService.getAllSessions());

        model.addAttribute("classRooms",
                classRoomService.getAllClassRooms());

        model.addAttribute("sections",
                sectionService.getAllSections());

    }

}