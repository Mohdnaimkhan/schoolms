package com.naim.school.academicsession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/academic-sessions")
public class AcademicSessionController {

    private final AcademicSessionService service;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */
    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Academic Sessions");
        model.addAttribute("academicSessions", service.getAllSessions());

        return "academicsession/list";
    }

    /*
     * ==========================================
     * ADD FORM
     * ==========================================
     */
    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Academic Session");
        model.addAttribute("academicSession", new AcademicSession());

        return "academicsession/form";
    }

    /*
     * ==========================================
     * EDIT FORM
     * ==========================================
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Academic Session");
        model.addAttribute("academicSession", service.getById(id));

        return "academicsession/form";
    }

    /*
     * ==========================================
     * SAVE
     * ==========================================
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute AcademicSession academicSession) {

        service.save(academicSession);

        return "redirect:/academic-sessions";
    }

    /*
     * ==========================================
     * SET CURRENT SESSION
     * ==========================================
     */
    @GetMapping("/current/{id}")
    public String setCurrent(@PathVariable Long id) {

        service.setCurrentSession(id);

        return "redirect:/academic-sessions";
    }

    /*
     * ==========================================
     * CLOSE SESSION FORM
     * ==========================================
     */
    @GetMapping("/close/{id}")
    public String closeForm(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Close Academic Session");
        model.addAttribute("academicSession", service.getById(id));

        return "academicsession/close-session";
    }

    /*
     * ==========================================
     * CLOSE SESSION
     * ==========================================
     */
    @PostMapping("/close")
    public String close(@ModelAttribute AcademicSession academicSession) {

        service.closeSession(
                academicSession.getId(),
                academicSession.getEndDate());

        return "redirect:/academic-sessions";
    }

}