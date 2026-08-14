package com.naim.school.fee;
import java.util.List;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.feehead.FeeHeadService;
import com.naim.school.studentsession.StudentSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;
    private final StudentSessionService studentSessionService;
    private final FeeHeadService feeHeadService;
    private final AcademicSessionService academicSessionService;

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

        model.addAttribute("fee", fee);

        loadMasters(model);

        return "fee/form";

    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model
    ) {

        if (fee.getAmount() != null && fee.getPaidAmount() != null
                && fee.getPaidAmount().compareTo(fee.getAmount()) > 0) {
            result.rejectValue("paidAmount", "invalid", "Paid amount cannot exceed the total amount.");
        }

        if (result.hasErrors()) {

            loadMasters(model);

            return "fee/form";

        }

        feeService.save(fee);

        return "redirect:/fees";

    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        Fee fee = feeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found"));

        model.addAttribute("fee", fee);

        loadMasters(model);

        return "fee/form";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        feeService.deleteById(id);

        return "redirect:/fees";

    }

    private void loadMasters(Model model) {

        model.addAttribute("studentSessions", studentSessionService.getCurrentStudents());

        model.addAttribute("feeHeads", feeHeadService.getAllFeeHeads());

    }

}
