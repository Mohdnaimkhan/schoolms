package com.naim.school.section;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.validation.BindingResult;
=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sections")
public class SectionController {

    private final SectionService service;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Sections");
<<<<<<< HEAD
        java.util.List<Section> sections = service.getAllSections();
        long sectionActive = sections.stream().filter(sec -> Boolean.TRUE.equals(sec.getActive())).count();
        long sectionDescribed = sections.stream().filter(sec -> sec.getDescription() != null && !sec.getDescription().isBlank()).count();
        model.addAttribute("sections", sections);
        model.addAttribute("sectionTotal", sections.size());
        model.addAttribute("sectionActive", sectionActive);
        model.addAttribute("sectionInactive", sections.size() - sectionActive);
        model.addAttribute("sectionDescribed", sectionDescribed);
=======
        model.addAttribute("sections", service.getAllSections());
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

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
<<<<<<< HEAD
    public String save(@Valid @ModelAttribute Section section, BindingResult result) {

        if (result.hasErrors()) {

            return "section/form";

        }
=======
    public String save(@Valid @ModelAttribute Section section) {
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

        service.save(section);

        return "redirect:/sections";

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

<<<<<<< HEAD
    @PostMapping("/status/{id}")
=======
    @GetMapping("/status/{id}")
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    public String changeStatus(@PathVariable Long id) {

        service.changeStatus(id);

        return "redirect:/sections";

    }

}