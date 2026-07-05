package com.naim.school.session.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.session.entity.AcademicSession;
import com.naim.school.session.service.AcademicSessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class AcademicSessionController {

    private final AcademicSessionService service;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Academic Sessions");
        model.addAttribute("sessions", service.getAllSessions());

        return "session/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Session");
        model.addAttribute("session", new AcademicSession());

        return "session/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Session");
        model.addAttribute("session", service.getById(id));

        return "session/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("session") AcademicSession session,
                       BindingResult result) {

        if (result.hasErrors()) {

            return "session/form";

        }

        service.save(session);

        return "redirect:/sessions";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/sessions";

    }

}