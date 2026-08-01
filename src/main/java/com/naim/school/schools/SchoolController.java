package com.naim.school.schools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings/school")
public class SchoolController {

    private final SchoolService service;

    public SchoolController(SchoolService service) {
        this.service = service;
    }

    @GetMapping
    public String profile(Model model) {

        School school = service.getSchool().orElse(new School());

        if (school.getSchoolCode() == null) {
            school.setSchoolCode("LOCAL001");
        }

        model.addAttribute("school", school);

        return "settings/school-profile";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute School school) {

        service.save(school);

        return "redirect:/settings/school?success";
    }

}