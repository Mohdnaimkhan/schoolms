package com.naim.school.academicsession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.naim.school.activitylog.ActivityLogService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/academic-sessions")
public class AcademicSessionController {

    private final AcademicSessionService service;
    private final ActivityLogService activityLogService;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */
    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Academic Sessions");
        java.util.List<AcademicSession> academicSessions = service.getAllSessions();
        long sessionCurrent = academicSessions.stream().filter(a -> Boolean.TRUE.equals(a.getCurrentSession())).count();
        long sessionClosed = academicSessions.stream().filter(a -> a.getEndDate() != null && a.getEndDate().isBefore(java.time.LocalDate.now())).count();
        model.addAttribute("academicSessions", academicSessions);
        model.addAttribute("sessionTotal", academicSessions.size());
        model.addAttribute("sessionCurrent", sessionCurrent);
        model.addAttribute("sessionClosed", sessionClosed);
        model.addAttribute("sessionOpen", academicSessions.size() - sessionClosed);

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
    public String save(@Valid @ModelAttribute AcademicSession academicSession, BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return "academicsession/form";

        }

        boolean isNew = academicSession.getId() == null;
        service.save(academicSession);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Academic session added successfully." : "Academic session updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Academic Session", "Added session " + academicSession.getSessionName());
        } else {
            activityLogService.logUpdate("Academic Session", "Updated session " + academicSession.getSessionName());
        }

        return "redirect:/academic-sessions";
    }

    /*
     * ==========================================
     * SET CURRENT SESSION
     * ==========================================
     */
    @GetMapping("/current/{id}")
    public String setCurrent(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        service.setCurrentSession(id);

        redirectAttributes.addFlashAttribute("success", "Current session updated successfully.");

        activityLogService.logStatusChange("Academic Session", "Set session #" + id + " as current session");

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
    public String close(@ModelAttribute AcademicSession academicSession, RedirectAttributes redirectAttributes) {

        service.closeSession(
                academicSession.getId(),
                academicSession.getEndDate());

        redirectAttributes.addFlashAttribute("success", "Academic session closed successfully.");

        activityLogService.logUpdate("Academic Session", "Closed session #" + academicSession.getId());

        return "redirect:/academic-sessions";
    }

}