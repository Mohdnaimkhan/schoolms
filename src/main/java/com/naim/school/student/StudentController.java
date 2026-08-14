package com.naim.school.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.studentsession.StudentSessionService;
import com.naim.school.attendance.Attendance;
import com.naim.school.attendance.AttendanceService;
import com.naim.school.attendance.AttendanceStatus;
import com.naim.school.fee.Fee;
import com.naim.school.fee.FeeService;
import com.naim.school.result.Result;
import com.naim.school.result.ResultRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    private final StudentSessionService studentSessionService;
    private final AttendanceService attendanceService;
    private final FeeService feeService;
    private final ResultRepository resultRepository;

    /*
     * ==========================================================
     * STUDENT LIST
     * ==========================================================
     */

    @GetMapping
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) StudentStatus status,
            Model model) {

        model.addAttribute("students", studentService.search(keyword, status));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("studentStatuses", StudentStatus.values());

        model.addAttribute("totalStudents", studentService.count());
        model.addAttribute("activeStudents", studentService.countActiveStudents());
        model.addAttribute("boys", studentService.countBoys());
        model.addAttribute("girls", studentService.countGirls());

        return "student/list";
    }

    /*
     * ==========================================================
     * ADD FORM
     * ==========================================================
     */

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Add Student");

        loadFormData(model);

        return "student/form";
    }

    /*
     * ==========================================================
     * EDIT FORM
     * ==========================================================
     */

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        model.addAttribute(
                "pageTitle",
                "Edit Student");

        loadFormData(model);

        return "student/form";
    }

    /*
     * ==========================================================
     * SAVE
     * ==========================================================
     */

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "pageTitle",
                    student.getId() == null
                            ? "Add Student"
                            : "Edit Student");

            loadFormData(model);

            return "student/form";
        }

        try {

            studentService.save(student, photoFile);

        } catch (IllegalArgumentException ex) {

            model.addAttribute("error", ex.getMessage());

            model.addAttribute(
                    "pageTitle",
                    student.getId() == null
                            ? "Add Student"
                            : "Edit Student");

            loadFormData(model);

            return "student/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Student saved successfully.");

        return "redirect:/students";
    }

    /*
     * ==========================================================
     * VIEW
     * ==========================================================
     */

    @GetMapping("/view/{id}")
    public String view(
            @PathVariable Long id,
            Model model) {

        Student student = studentService.getById(id);

        model.addAttribute("student", student);

        var currentSession = studentSessionService.getCurrentSession(student);
        model.addAttribute("currentSession", currentSession);
        model.addAttribute("studentSessions", studentSessionService.getStudentHistory(student));

        List<Attendance> attendanceRecords = attendanceService.findByStudent(student);
        if (currentSession != null) {
            attendanceRecords = attendanceRecords.stream()
                    .filter(a -> a.getAcademicSession() != null
                            && a.getAcademicSession().getId().equals(currentSession.getAcademicSession().getId()))
                    .toList();
        }
        long presentCount = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absentCount = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long leaveCount = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.LEAVE).count();
        long attendanceTotal = attendanceRecords.size();
        double attendancePercentage = attendanceTotal == 0 ? 0D : (presentCount * 100D / attendanceTotal);

        List<Fee> fees = currentSession == null ? List.of() : feeService.findByStudentSession(currentSession);
        BigDecimal totalFees = fees.stream().map(Fee::getAmount).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidFees = fees.stream().map(Fee::getPaidAmount).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dueFees = fees.stream().map(Fee::getDueAmount).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Result> results = currentSession == null ? List.of() : resultRepository.findByStudentSession(currentSession);
        BigDecimal obtainedMarks = results.stream().map(Result::getMarksObtained).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        int maxMarks = results.stream()
                .filter(r -> r.getExamSubject() != null && r.getExamSubject().getMaxMarks() != null)
                .mapToInt(r -> r.getExamSubject().getMaxMarks()).sum();
        double resultPercentage = maxMarks == 0 ? 0D : obtainedMarks.doubleValue() * 100D / maxMarks;

        model.addAttribute("attendanceRecords", attendanceRecords);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        model.addAttribute("leaveCount", leaveCount);
        model.addAttribute("attendanceTotal", attendanceTotal);
        model.addAttribute("attendancePercentage", attendancePercentage);
        model.addAttribute("fees", fees);
        model.addAttribute("totalFees", totalFees);
        model.addAttribute("paidFees", paidFees);
        model.addAttribute("dueFees", dueFees);
        model.addAttribute("results", results);
        model.addAttribute("obtainedMarks", obtainedMarks);
        model.addAttribute("maxMarks", maxMarks);
        model.addAttribute("resultPercentage", resultPercentage);

        return "student/view";
    }

    /*
     * ==========================================================
     * PRINT
     * ==========================================================
     */

    @GetMapping("/print/{id}")
    public String print(
            @PathVariable Long id,
            Model model) {

        Student student = studentService.getById(id);

        model.addAttribute("student", student);

        model.addAttribute("currentSession",
                studentSessionService.getCurrentSession(student));

        return "student/print";
    }

    /*
     * ==========================================================
     * CHANGE STATUS
     * ==========================================================
     */

    @PostMapping("/status/{id}")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam StudentStatus status,
            RedirectAttributes redirectAttributes) {

        studentService.changeStatus(id, status);

        redirectAttributes.addFlashAttribute(
                "success",
                "Student status updated successfully.");

        return "redirect:/students";
    }

    /*
     * ==========================================================
     * AADHAAR DUPLICATE CHECK
     * ==========================================================
     */

    @GetMapping("/check/aadhaar")
    @ResponseBody
    public boolean checkAadhaar(
            @RequestParam String value,
            @RequestParam(required = false) Long id) {

        return studentService.existsAadhaar(
                value,
                id);
    }

    @GetMapping("/id-card/{id}")
    public String idCard(@PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        return "student/id-card";

    }
    /*
     * ==========================================================
     * LOAD FORM DATA
     * ==========================================================
     */

    private void loadFormData(Model model) {

        model.addAttribute("genders", Gender.values());
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("religions", Religion.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("studentStatuses", StudentStatus.values());

    }

}