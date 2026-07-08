package com.naim.school.fee;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.student.StudentService;
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
    private final StudentService studentService;
    private final AcademicSessionService academicSessionService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("feeList", feeService.findAll());

        return "fee/list";

    }

    @GetMapping("/new")
    public String createForm(Model model) {

        Fee fee = new Fee();

        fee.setPaymentDate(LocalDate.now());

        fee.setStatus(FeeStatus.PENDING);

        model.addAttribute("fee", fee);

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("academic", academicSessionService.getAllSessions());

        model.addAttribute("statuses", FeeStatus.values());

        return "fee/form";

    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("fee") Fee fee,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("students", studentService.getAllStudents());

            model.addAttribute("academic-session", academicSessionService.getAllSessions());

            model.addAttribute("statuses", FeeStatus.values());

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

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("academic-session", academicSessionService.getAllSessions());

        model.addAttribute("statuses", FeeStatus.values());

        return "fee/form";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        feeService.deleteById(id);

        return "redirect:/fees";

    }

}