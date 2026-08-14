package com.naim.school.exam;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.studentsession.StudentSessionService;
import com.naim.school.subject.SubjectService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    private final AcademicSessionService academicSessionService;

    private final ClassRoomService classRoomService;

    private final SubjectService subjectService;

    private final StudentSessionService studentSessionService;

    // ===========================
    // List
    // ===========================

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Exams");

        List<Exam> exams = sessionId == null
                ? examService.getAll()
                : examService.getByAcademicSession(sessionId);
        java.time.LocalDate today = java.time.LocalDate.now();
        long examUpcoming = exams.stream().filter(e -> e.getExamDate() != null && e.getExamDate().isAfter(today)).count();
        long examCompleted = exams.stream().filter(e -> e.getExamDate() != null && !e.getExamDate().isAfter(today)).count();
        long examUndated = exams.stream().filter(e -> e.getExamDate() == null).count();
        model.addAttribute("exams", exams);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("examTotal", exams.size());
        model.addAttribute("examUpcoming", examUpcoming);
        model.addAttribute("examCompleted", examCompleted);
        model.addAttribute("examUndated", examUndated);

        return "exam/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Exam");

        model.addAttribute("exam", new Exam());

        loadMasters(model);

        return "exam/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Exam");

        model.addAttribute("exam", examService.getById(id));

        loadMasters(model);

        return "exam/form";

    }

    // ===========================
    // Save
    // ===========================

    @PostMapping("/save")
    public String save(@ModelAttribute Exam exam) {

        Exam saved = examService.save(exam);

        return "redirect:/exams/view/" + saved.getId();

    }

    // ===========================
    // Delete
    // ===========================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        examService.delete(id);

        return "redirect:/exams";

    }

    // ===========================
    // View (Exam Subjects + Marks Entry Links)
    // ===========================

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Exam Details");

        Exam exam = examService.getById(id);

        model.addAttribute("exam", exam);

        model.addAttribute("examSubjects", examService.getSubjects(id));

        model.addAttribute("subjects", subjectService.getAllSubjects());

        model.addAttribute("students",
                studentSessionService.getStudentsBySessionAndClass(
                        exam.getAcademicSessionId(),
                        exam.getClassRoomId()));

        return "exam/view";

    }

    // ===========================
    // Add a Subject to this Exam
    // ===========================

    @PostMapping("/{examId}/subjects/add")
    public String addSubject(
            @PathVariable Long examId,
            @RequestParam Long subjectId,
            @RequestParam(required = false) Integer maxMarks,
            @RequestParam(required = false) Integer passMarks) {

        examService.addSubject(examId, subjectId, maxMarks, passMarks);

        return "redirect:/exams/view/" + examId;

    }

    // ===========================
    // Remove a Subject from an Exam
    // ===========================

    @PostMapping("/{examId}/subjects/delete/{examSubjectId}")
    public String deleteSubject(
            @PathVariable Long examId,
            @PathVariable Long examSubjectId) {

        examService.deleteSubject(examSubjectId);

        return "redirect:/exams/view/" + examId;

    }

    // ===========================
    // Common Master Data
    // ===========================

    private void loadMasters(Model model) {

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

    }

}
