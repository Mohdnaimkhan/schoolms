package com.naim.school.teachersession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.section.SectionService;
import com.naim.school.subject.SubjectService;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher-sessions")
public class TeacherSessionController {

    private final TeacherSessionService teacherSessionService;

    private final TeacherService teacherService;

    private final AcademicSessionService academicSessionService;

    private final SubjectService subjectService;

    private final ClassRoomService classRoomService;

    private final SectionService sectionService;

    // ===========================
    // List
    // ===========================

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Teacher Sessions");

        List<TeacherSession> teacherSessions = sessionId == null
                ? teacherSessionService.getAll()
                : teacherSessionService.getByAcademicSession(sessionId);
        long teacherSessionCurrent = teacherSessions.stream().filter(t -> Boolean.TRUE.equals(t.getCurrentSession())).count();
        long teacherSessionComplete = teacherSessions.stream().filter(t -> t.getTeacher() != null && t.getClassRoom() != null && t.getSubject() != null).count();
        long teacherSessionSections = teacherSessions.stream().filter(t -> t.getSection() != null).count();
        model.addAttribute("teacherSessions", teacherSessions);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("teacherSessionTotal", teacherSessions.size());
        model.addAttribute("teacherSessionCurrent", teacherSessionCurrent);
        model.addAttribute("teacherSessionComplete", teacherSessionComplete);
        model.addAttribute("teacherSessionSections", teacherSessionSections);

        return "teachersession/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Teacher Session");

        model.addAttribute("teacherSession", new TeacherSession());

        loadMasters(model);

        return "teachersession/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Teacher Session");

        model.addAttribute("teacherSession",
                teacherSessionService.getById(id));

        loadMasters(model);

        return "teachersession/form";

    }

    // ===========================
    // Save
    // ===========================

    @PostMapping("/save")
    public String save(@ModelAttribute TeacherSession teacherSession) {

        teacherSessionService.save(teacherSession);

        return "redirect:/teacher-sessions";

    }

    // ===========================
    // Delete
    // ===========================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        teacherSessionService.delete(id);

        return "redirect:/teacher-sessions";

    }

    // ===========================
    // Teacher History
    // ===========================

    @GetMapping("/history/{teacherId}")
    public String history(@PathVariable Long teacherId, Model model) {

        model.addAttribute("pageTitle", "Teacher History");

        Teacher teacher = teacherService.getById(teacherId);

        model.addAttribute("teacher", teacher);

        model.addAttribute("history",
                teacherSessionService.getTeacherHistory(teacher));

        return "teachersession/history";

    }

    // ===========================
    // Reassign - Step 1: Select Teacher / From / To
    // ===========================

    @GetMapping("/reassign")
    public String reassignForm(Model model) {

        model.addAttribute("pageTitle", "Reassign Teacher");

        model.addAttribute("teachers", teacherService.getAllTeachers());

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        return "teachersession/reassign";

    }

    // ===========================
    // Reassign - Step 2: Review Assignments
    // ===========================

    @GetMapping("/reassign/review")
    public String reassignReview(
            @RequestParam Long teacherId,
            @RequestParam Long fromSessionId,
            @RequestParam Long toSessionId,
            Model model) {

        model.addAttribute("pageTitle", "Reassign Teacher - Review");

        Teacher teacher = teacherService.getById(teacherId);

        model.addAttribute("teacher", teacher);

        model.addAttribute("assignments",
                teacherSessionService.getTeacherAssignmentsInSession(teacher, fromSessionId));

        model.addAttribute("toSessionId", toSessionId);

        model.addAttribute("toSession", academicSessionService.getById(toSessionId));

        return "teachersession/reassign-review";

    }

    // ===========================
    // Reassign - Step 3: Execute
    // ===========================

    @PostMapping("/reassign/save")
    public String reassignSave(
            @RequestParam(value = "teacherSessionIds", required = false) List<Long> teacherSessionIds,
            @RequestParam Long toSessionId) {

        if (teacherSessionIds != null && !teacherSessionIds.isEmpty()) {

            teacherSessionService.reassignToSession(teacherSessionIds, toSessionId);

        }

        return "redirect:/teacher-sessions?sessionId=" + toSessionId;

    }

    // ===========================
    // Common Master Data
    // ===========================

    private void loadMasters(Model model) {

        model.addAttribute("teachers",
                teacherService.getAllTeachers());

        model.addAttribute("academicSessions",
                academicSessionService.getAllSessions());

        model.addAttribute("subjects",
                subjectService.getAllSubjects());

        model.addAttribute("classRooms",
                classRoomService.getAllClassRooms());

        model.addAttribute("sections",
                sectionService.getAllSections());

    }

}
