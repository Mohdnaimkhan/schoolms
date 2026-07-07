package com.naim.school.academicsession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/academic-sessions")
public class AcademicSessionController {

    private final AcademicSessionService service;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Academic Sessions");
        model.addAttribute("academicSessions", service.getAllSessions());

        return "academicsession/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Session");
        model.addAttribute("academicSession", new AcademicSession());

        return "academicsession/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Session");
        model.addAttribute("academicSession", service.getById(id));

        return "academicsession/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("academicSession") AcademicSession session,
                       BindingResult result) {

        if (result.hasErrors()) {

            return "academicsession/form";

        }

        service.save(session);

        return "redirect:/academic-sessions";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/academic-sessions";

    }

}