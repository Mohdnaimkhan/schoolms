package com.naim.school.studentsession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.student.Student;
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
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Student Sessions");

        List<StudentSession> studentSessions = sessionId == null
                ? studentSessionService.getAll()
                : studentSessionService.getByAcademicSession(sessionId);
        long studentSessionActive = studentSessions.stream().filter(s -> s.getStatus() == com.naim.school.student.StudentStatus.ACTIVE).count();
        long studentSessionCurrent = studentSessions.stream().filter(s -> Boolean.TRUE.equals(s.getCurrentSession())).count();
        long studentSessionAssigned = studentSessions.stream().filter(s -> s.getClassRoom() != null && s.getSection() != null).count();
        model.addAttribute("studentSessions", studentSessions);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("studentSessionTotal", studentSessions.size());
        model.addAttribute("studentSessionActive", studentSessionActive);
        model.addAttribute("studentSessionCurrent", studentSessionCurrent);
        model.addAttribute("studentSessionAssigned", studentSessionAssigned);

        return "studentsession/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
    public String add(
            @RequestParam(value = "studentId", required = false) Long studentId,
            Model model) {

        model.addAttribute("pageTitle", "Add Student Session");

        StudentSession studentSession = new StudentSession();

        if (studentId != null) {

            studentSession.setStudentId(studentId);

        }

        model.addAttribute("studentSession", studentSession);

        loadMasters(model);

        return "studentsession/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Student Session");

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

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        studentSessionService.delete(id);

        return "redirect:/student-sessions";

    }

    // ===========================
    // Promote - Step 1: Select From / To
    // ===========================

    @GetMapping("/promote")
    public String promoteForm(Model model) {

        model.addAttribute("pageTitle", "Promote Students");

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

        model.addAttribute("sections", sectionService.getAllSections());

        return "studentsession/promote";

    }

    // ===========================
    // Promote - Step 2: Review Students
    // ===========================

    @GetMapping("/promote/review")
    public String promoteReview(
            @RequestParam Long fromSessionId,
            @RequestParam Long fromClassRoomId,
            @RequestParam(required = false) Long fromSectionId,
            @RequestParam Long toSessionId,
            @RequestParam Long toClassRoomId,
            @RequestParam(required = false) Long toSectionId,
            Model model) {

        model.addAttribute("pageTitle", "Promote Students - Review");

        List<StudentSession> students =
                studentSessionService.getStudentsBySessionAndClass(fromSessionId, fromClassRoomId);

        if (fromSectionId != null) {

            students = students.stream()
                    .filter(s -> s.getSection() != null
                            && fromSectionId.equals(s.getSection().getId()))
                    .toList();

        }

        model.addAttribute("students", students);

        model.addAttribute("toSessionId", toSessionId);
        model.addAttribute("toClassRoomId", toClassRoomId);
        model.addAttribute("toSectionId", toSectionId);

        model.addAttribute("toSession", academicSessionService.getById(toSessionId));
        model.addAttribute("toClassRoom", classRoomService.getById(toClassRoomId));
        model.addAttribute("toSectionName",
                toSectionId == null ? null : sectionService.getById(toSectionId).getSectionName());

        return "studentsession/promote-review";

    }

    // ===========================
    // Promote - Step 3: Execute
    // ===========================

    @PostMapping("/promote/save")
    public String promoteSave(
            @RequestParam(value = "studentSessionIds", required = false) List<Long> studentSessionIds,
            @RequestParam Long toSessionId,
            @RequestParam Long toClassRoomId,
            @RequestParam(required = false) Long toSectionId) {

        if (studentSessionIds != null && !studentSessionIds.isEmpty()) {

            studentSessionService.promoteStudents(
                    studentSessionIds,
                    toSessionId,
                    toClassRoomId,
                    toSectionId);

        }

        return "redirect:/student-sessions?sessionId=" + toSessionId;

    }

    // ===========================
    // Student History
    // ===========================

    @GetMapping("/history/{studentId}")
    public String history(@PathVariable Long studentId, Model model) {

        model.addAttribute("pageTitle", "Student History");

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        model.addAttribute("history",
                studentSessionService.getStudentHistory(student));

        return "studentsession/history";

    }

    // ===========================
    // Common Master Data
    // ===========================

    private void loadMasters(Model model) {

        model.addAttribute("students",
                studentService.getAllStudents());

        model.addAttribute("academicSessions",
                academicSessionService.getAllSessions());

        model.addAttribute("classRooms",
                classRoomService.getAllClassRooms());

        model.addAttribute("sections",
                sectionService.getAllSections());

        model.addAttribute("studentStatuses",
                com.naim.school.student.StudentStatus.values());

    }

}