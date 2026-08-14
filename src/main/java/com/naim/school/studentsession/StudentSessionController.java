package com.naim.school.studentsession;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import com.naim.school.student.Student;
=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
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
<<<<<<< HEAD
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
=======
    public String list(Model model) {

        model.addAttribute("studentSessions",
                studentSessionService.getAll());
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

        return "studentsession/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
<<<<<<< HEAD
    public String add(
            @RequestParam(value = "studentId", required = false) Long studentId,
            Model model) {

        model.addAttribute("pageTitle", "Add Student Session");

        StudentSession studentSession = new StudentSession();

        if (studentId != null) {

            studentSession.setStudentId(studentId);

        }

        model.addAttribute("studentSession", studentSession);
=======
    public String add(Model model) {

        model.addAttribute("studentSession",
                new StudentSession());
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

        loadMasters(model);

        return "studentsession/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

<<<<<<< HEAD
        model.addAttribute("pageTitle", "Edit Student Session");

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
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

<<<<<<< HEAD
    @PostMapping("/delete/{id}")
=======
    @GetMapping("/delete/{id}")
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    public String delete(@PathVariable Long id) {

        studentSessionService.delete(id);

        return "redirect:/student-sessions";

    }

    // ===========================
<<<<<<< HEAD
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
=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    // Common Master Data
    // ===========================

    private void loadMasters(Model model) {

        model.addAttribute("students",
<<<<<<< HEAD
                studentService.getAllStudents());
=======
                studentService.countActiveStudents());
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

        model.addAttribute("academicSessions",
                academicSessionService.getAllSessions());

        model.addAttribute("classRooms",
                classRoomService.getAllClassRooms());

        model.addAttribute("sections",
                sectionService.getAllSections());

<<<<<<< HEAD
        model.addAttribute("studentStatuses",
                com.naim.school.student.StudentStatus.values());

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    }

}