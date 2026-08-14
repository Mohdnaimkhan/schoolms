package com.naim.school.salary;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.security.CurrentUserService;
import com.naim.school.security.Role;
import com.naim.school.teacher.TeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/salary")
public class SalaryController {
    private final SalaryService service;
    private final TeacherService teacherService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public String list(@RequestParam(required = false) Long teacherId,
                       @RequestParam(required = false) String status, Model model) {
        List<SalaryLedger> ledgers;
        if (currentUserService.hasRole(Role.TEACHER)) {
            var user = currentUserService.getCurrentUser();
            if (user.getTeacher() == null) throw new RuntimeException("Teacher account is not linked to a teacher profile.");
            ledgers = service.getByTeacher(user.getTeacher().getId());
        } else {
            ledgers = teacherId == null ? service.getAll() : service.getByTeacher(teacherId);
            model.addAttribute("teachers", teacherService.getActiveTeachers());
        }
        if (status != null && !status.isBlank()) {
            ledgers = ledgers.stream().filter(l -> status.equalsIgnoreCase(l.getStatus())).toList();
        }
        BigDecimal totalNet = ledgers.stream().map(SalaryLedger::getNetSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = ledgers.stream().map(SalaryLedger::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("ledgers", ledgers);
        model.addAttribute("selectedTeacherId", teacherId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("totalNet", totalNet);
        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("totalDue", totalNet.subtract(totalPaid).max(BigDecimal.ZERO));
        model.addAttribute("pageTitle", "Teacher Salary Ledger");
        return "salary/list";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("ledger", new SalaryLedger());
        model.addAttribute("teachers", teacherService.getActiveTeachers());
        model.addAttribute("pageTitle", "Add Salary Ledger");
        return "salary/form";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("ledger", service.getLedger(id));
        model.addAttribute("teachers", teacherService.getActiveTeachers());
        model.addAttribute("pageTitle", "Edit Salary Ledger");
        return "salary/form";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("ledger") SalaryLedger ledger, BindingResult result,
                       @RequestParam Long teacherId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.getActiveTeachers());
            model.addAttribute("pageTitle", "Salary Ledger");
            return "salary/form";
        }
        service.saveLedger(ledger, teacherId);
        return "redirect:/salary";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) { service.deleteLedger(id); return "redirect:/salary"; }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/{id}/payment/add")
    public String payment(@PathVariable Long id, Model model) {
        model.addAttribute("ledger", service.getLedger(id));
        model.addAttribute("payment", new SalaryPayment());
        model.addAttribute("pageTitle", "Salary Payment");
        return "salary/payment";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/{id}/payment/save")
    public String paymentSave(@PathVariable Long id, @Valid @ModelAttribute("payment") SalaryPayment payment,
                              BindingResult result, Model model) {
        SalaryLedger ledger = service.getLedger(id);
        if (result.hasErrors()) {
            model.addAttribute("ledger", ledger);
            model.addAttribute("pageTitle", "Salary Payment");
            return "salary/payment";
        }
        service.savePayment(id, payment);
        return "redirect:/salary/" + id + "/view";
    }

    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        SalaryLedger ledger = service.getLedger(id);
        if (currentUserService.hasRole(Role.TEACHER)) {
            var user = currentUserService.getCurrentUser();
            if (user.getTeacher() == null || !user.getTeacher().getId().equals(ledger.getTeacher().getId())) {
                throw new org.springframework.security.access.AccessDeniedException("Not allowed");
            }
        }
        model.addAttribute("ledger", ledger);
        model.addAttribute("pageTitle", "Salary Ledger");
        return "salary/view";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/payment/edit/{id}")
    public String paymentEdit(@PathVariable Long id, Model model) {
        SalaryPayment payment = service.getPayment(id);
        model.addAttribute("ledger", payment.getSalaryLedger());
        model.addAttribute("payment", payment);
        model.addAttribute("pageTitle", "Edit Salary Payment");
        return "salary/payment";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/payment/delete/{id}")
    public String paymentDelete(@PathVariable Long id, @RequestParam Long ledgerId) {
        service.deletePayment(id);
        return "redirect:/salary/" + ledgerId + "/view";
    }
}
