package com.naim.school.schools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/settings/school")
public class SchoolController {

    private final SchoolService service;

    public SchoolController(SchoolService service) {
        this.service = service;
    }

    /*
     * =========================================
     * School Profile
     * =========================================
     */

    @GetMapping
    public String profile(Model model) {

        School school = service.getSchool().orElse(new School());

        if (school.getSchoolCode() == null || school.getSchoolCode().isBlank()) {

            school.setSchoolCode("LOCAL001");

        }

        model.addAttribute("school", school);

        return "settings/school-profile";

    }

    /*
     * =========================================
     * Save / Update
     * =========================================
     */

    @PostMapping("/save")
    public String save(@ModelAttribute School school,
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestParam(value = "signatureFile", required = false) MultipartFile signatureFile) {

        service.save(school, logoFile, signatureFile);

        return "redirect:/settings/school?success";

    }



}