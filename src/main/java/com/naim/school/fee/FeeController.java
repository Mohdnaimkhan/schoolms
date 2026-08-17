package com.naim.school.fee;

import com.naim.school.sms.BusinessException;
import java.util.List;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.feehead.FeeHeadService;
import com.naim.school.studentsession.StudentSessionService;
import com.naim.school.activitylog.ActivityLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;
    private final StudentSessionService studentSessionService;
    private final FeeHeadService feeHeadService;
    private final AcademicSessionService academicSessionService;
    private final ActivityLogService activityLogService;

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        List<Fee> feeList = sessionId == null
                ? feeService.findAll()
                : feeService.findByAcademicSession(academicSessionService.getById(sessionId));
        long feePaid = feeList.stream().filter(f -> f.getStatus() == FeeStatus.PAID).count();
        long feePartial = feeList.stream().filter(f -> f.getStatus() == FeeStatus.PARTIAL).count();
        long feePending = feeList.stream().filter(f -> f.getStatus() == FeeStatus.PENDING).count();
        model.addAttribute("feeList", feeList);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("feeTotal", feeList.size());
        model.addAttribute("feePaid", feePaid);
        model.addAttribute("feePartial", feePartial);
        model.addAttribute("feePending", feePending);

        return "fee/list";

    }

    @GetMapping("/new")
    public String createForm(Model model) {

        Fee fee = new Fee();

        fee.setPaymentDate(LocalDate.now());

        fee.setStatus(FeeStatus.PENDING);

        model.addAttribute("fee", fee);

        loadMasters(model);

        return "fee/form";

    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (fee.getAmount() != null && fee.getPaidAmount() != null) {
            if (fee.getPaidAmount().compareTo(fee.getAmount()) > 0) {
                result.rejectValue("paidAmount", "invalid", "Paid amount cannot exceed the total amount.");
            }
            fee.setDueAmount(fee.getAmount().subtract(fee.getPaidAmount()));
        }

        if (result.hasErrors()) {

            loadMasters(model);

            return "fee/form";

        }

        boolean isNew = fee.getId() == null;
        feeService.save(fee);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Fee record added successfully." : "Fee record updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Fee", "Added fee record (Receipt: " + fee.getReceiptNo() + ")");
        } else {
            activityLogService.logUpdate("Fee", "Updated fee record (Receipt: " + fee.getReceiptNo() + ")");
        }

        return "redirect:/fees";

    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        Fee fee = feeService.findById(id)
                .orElseThrow(() -> new BusinessException("Fee not found"));

        model.addAttribute("fee", fee);

        loadMasters(model);

        return "fee/form";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        feeService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Fee record deleted successfully.");

        activityLogService.logDelete("Fee", "Deleted fee record #" + id);

        return "redirect:/fees";

    }

    private void loadMasters(Model model) {

        model.addAttribute("studentSessions", studentSessionService.getCurrentStudents());

        model.addAttribute("feeHeads", feeHeadService.getAllFeeHeads());

        model.addAttribute("statuses", FeeStatus.values());

    }

}
