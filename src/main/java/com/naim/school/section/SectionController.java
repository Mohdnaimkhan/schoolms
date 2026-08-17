package com.naim.school.section;

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
@RequestMapping("/sections")
public class SectionController {

    private final SectionService service;
    private final ActivityLogService activityLogService;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Sections");
        java.util.List<Section> sections = service.getAllSections();
        long sectionActive = sections.stream().filter(sec -> Boolean.TRUE.equals(sec.getActive())).count();
        long sectionDescribed = sections.stream().filter(sec -> sec.getDescription() != null && !sec.getDescription().isBlank()).count();
        model.addAttribute("sections", sections);
        model.addAttribute("sectionTotal", sections.size());
        model.addAttribute("sectionActive", sectionActive);
        model.addAttribute("sectionInactive", sections.size() - sectionActive);
        model.addAttribute("sectionDescribed", sectionDescribed);

        return "section/list";

    }

    /*
     * ==========================================
     * ADD
     * ==========================================
     */

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Section");
        model.addAttribute("section", new Section());

        return "section/form";

    }

    /*
     * ==========================================
     * EDIT
     * ==========================================
     */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Section");
        model.addAttribute("section", service.getById(id));

        return "section/form";

    }

    /*
     * ==========================================
     * SAVE
     * ==========================================
     */

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Section section, BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return "section/form";

        }

        boolean isNew = section.getId() == null;
        service.save(section);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Section added successfully." : "Section updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Section", "Added section " + section.getSectionName());
        } else {
            activityLogService.logUpdate("Section", "Updated section " + section.getSectionName());
        }

        return "redirect:/sections";

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        service.changeStatus(id);

        redirectAttributes.addFlashAttribute("success", "Section status updated successfully.");

        activityLogService.logStatusChange("Section", "Changed status for section #" + id);

        return "redirect:/sections";

    }

}