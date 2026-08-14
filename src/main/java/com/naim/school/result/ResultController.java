package com.naim.school.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.exam.ExamSubject;
import com.naim.school.studentsession.StudentSessionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;

    private final StudentSessionService studentSessionService;

    // ===========================
    // Marks Entry - one subject, whole class
    // ===========================

    @GetMapping("/entry/{examSubjectId}")
    public String entry(@PathVariable Long examSubjectId, Model model) {

        model.addAttribute("pageTitle", "Marks Entry");

        ExamSubject examSubject = resultService.getExamSubject(examSubjectId);

        model.addAttribute("examSubject", examSubject);

        model.addAttribute("students",
                resultService.getStudentsForEntry(examSubject));

        model.addAttribute("existingMarks",
                resultService.getExistingMarks(examSubject));

        return "result/entry";

    }

    @PostMapping("/entry/save")
    public String saveEntry(
            @RequestParam Long examSubjectId,
            @RequestParam(value = "studentSessionIds", required = false) List<Long> studentSessionIds,
            @RequestParam(value = "marks", required = false) List<String> marks) {

        resultService.saveMarks(examSubjectId, studentSessionIds, marks);

        ExamSubject examSubject = resultService.getExamSubject(examSubjectId);

        return "redirect:/exams/view/" + examSubject.getExam().getId();

    }

    // ===========================
    // Report Card
    // ===========================

    @GetMapping("/report-card/{studentSessionId}/{examId}")
    public String reportCard(
            @PathVariable Long studentSessionId,
            @PathVariable Long examId,
            Model model) {

        model.addAttribute("pageTitle", "Report Card");

        List<Result> results = resultService.getReportCard(studentSessionId, examId);

        model.addAttribute("results", results);

        model.addAttribute("studentSession",
                studentSessionService.getById(studentSessionId));

        BigDecimal totalObtained = BigDecimal.ZERO;
        int totalMax = 0;

        for (Result r : results) {

            totalObtained = totalObtained.add(r.getMarksObtained());

            if (r.getExamSubject() != null && r.getExamSubject().getMaxMarks() != null) {

                totalMax += r.getExamSubject().getMaxMarks();

            }

        }

        model.addAttribute("totalObtained", totalObtained);
        model.addAttribute("totalMax", totalMax);

        model.addAttribute("percentage",
                totalMax > 0
                        ? totalObtained.multiply(BigDecimal.valueOf(100))
                                .divide(BigDecimal.valueOf(totalMax), 2, RoundingMode.HALF_UP)
                        : null);

        return "result/report-card";

    }

}
