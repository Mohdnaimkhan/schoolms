package com.naim.school.salary;

import com.naim.school.sms.BusinessException;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.security.CurrentUserService;
import com.naim.school.security.Role;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherService;
import com.naim.school.activitylog.ActivityLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/salary")
public class SalaryController {
    private final SalaryService service;
    private final TeacherService teacherService;
    private final CurrentUserService currentUserService;
    private final ActivityLogService activityLogService;

    @GetMapping
    public String list(@RequestParam(required = false) Long teacherId,
                       @RequestParam(required = false) String status, Model model) {
        List<SalaryLedger> ledgers;
        if (currentUserService.hasRole(Role.TEACHER)) {
            var user = currentUserService.getCurrentUser();
            if (user.getTeacher() == null) throw new BusinessException("Teacher account is not linked to a teacher profile.");
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
                       @RequestParam Long teacherId, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.getActiveTeachers());
            model.addAttribute("pageTitle", "Salary Ledger");
            return "salary/form";
        }
        boolean isNew = ledger.getId() == null;
        service.saveLedger(ledger, teacherId);
        redirectAttributes.addFlashAttribute("success",
                isNew ? "Salary ledger added successfully." : "Salary ledger updated successfully.");
        Teacher teacher = teacherService.getById(teacherId);
        if (isNew) {
            activityLogService.logCreate("Salary Ledger", "Added salary ledger for " + teacher.getTeacherName());
        } else {
            activityLogService.logUpdate("Salary Ledger", "Updated salary ledger for " + teacher.getTeacherName());
        }
        return "redirect:/salary";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        SalaryLedger ledger = service.getLedger(id);
        service.deleteLedger(id);
        redirectAttributes.addFlashAttribute("success", "Salary ledger deleted successfully.");
        activityLogService.logDelete("Salary Ledger", "Deleted salary ledger for " + ledger.getTeacher().getTeacherName());
        return "redirect:/salary";
    }

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
                              BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        SalaryLedger ledger = service.getLedger(id);
        if (result.hasErrors()) {
            model.addAttribute("ledger", ledger);
            model.addAttribute("pageTitle", "Salary Payment");
            return "salary/payment";
        }
        boolean isNew = payment.getId() == null;
        service.savePayment(id, payment);
        redirectAttributes.addFlashAttribute("success",
                isNew ? "Payment recorded successfully." : "Payment updated successfully.");
        if (isNew) {
            activityLogService.logCreate("Salary Payment", "Recorded payment of " + payment.getAmount()
                    + " for " + ledger.getTeacher().getTeacherName());
        } else {
            activityLogService.logUpdate("Salary Payment", "Updated payment for " + ledger.getTeacher().getTeacherName());
        }
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
    public String paymentDelete(@PathVariable Long id, @RequestParam Long ledgerId, RedirectAttributes redirectAttributes) {
        service.deletePayment(id);
        redirectAttributes.addFlashAttribute("success", "Payment deleted successfully.");
        activityLogService.logDelete("Salary Payment", "Deleted payment #" + id + " from ledger #" + ledgerId);
        return "redirect:/salary/" + ledgerId + "/view";
    }
}
