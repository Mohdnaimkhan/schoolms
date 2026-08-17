package com.naim.school.certificate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.student.Student;
import com.naim.school.student.StudentService;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.activitylog.ActivityLogService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    private final StudentService studentService;

    private final ActivityLogService activityLogService;

    // ===========================
    // Bonafide Certificate
    // ===========================

    @GetMapping("/bonafide/{studentId}")
    public String bonafide(@PathVariable Long studentId, Model model) {

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        model.addAttribute("studentSession",
                certificateService.getRelevantSession(studentId));

        model.addAttribute("issueDate", LocalDate.now());

        activityLogService.logCreate("Certificate", "Generated bonafide certificate for " + student.getStudentName());

        return "certificate/bonafide";

    }

    // ===========================
    // Character Certificate
    // ===========================

    @GetMapping("/character/{studentId}")
    public String character(@PathVariable Long studentId, Model model) {

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        model.addAttribute("studentSession",
                certificateService.getRelevantSession(studentId));

        model.addAttribute("issueDate", LocalDate.now());

        activityLogService.logCreate("Certificate", "Generated character certificate for " + student.getStudentName());

        return "certificate/character";

    }

    // ===========================
    // Transfer Certificate - List / Issue History
    // ===========================

    @GetMapping("/tc/{studentId}")
    public String tcList(@PathVariable Long studentId, Model model) {

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        List<TransferCertificate> tcList = certificateService.getTcHistory(studentId);

        model.addAttribute("tcList", tcList);

        return "certificate/tc-list";

    }

    // ===========================
    // Transfer Certificate - Issue New
    // ===========================

    @GetMapping("/tc/{studentId}/new")
    public String tcForm(@PathVariable Long studentId, Model model) {

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        StudentSession session = certificateService.getRelevantSession(studentId);

        model.addAttribute("studentSession", session);

        TransferCertificate tc = new TransferCertificate();

        if (session != null) {

            tc.setStudentSessionId(session.getId());

        }

        model.addAttribute("tc", tc);

        return "certificate/tc-form";

    }

    @PostMapping("/tc/save")
    public String tcSave(@ModelAttribute TransferCertificate tc, RedirectAttributes redirectAttributes) {

        TransferCertificate saved = certificateService.save(tc);

        redirectAttributes.addFlashAttribute("success", "Transfer certificate issued successfully.");

        activityLogService.logCreate("Certificate", "Issued transfer certificate #" + saved.getId());

        return "redirect:/students/certificate/tc/print/" + saved.getId();

    }

    // ===========================
    // Transfer Certificate - Print
    // ===========================

    @GetMapping("/tc/print/{tcId}")
    public String tcPrint(@PathVariable Long tcId, Model model) {

        TransferCertificate tc = certificateService.getTcById(tcId);

        model.addAttribute("tc", tc);

        return "certificate/tc-print";

    }

}
