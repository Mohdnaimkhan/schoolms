package com.naim.school.schools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.activitylog.ActivityLogService;

@Controller
@RequestMapping("/settings/school")
public class SchoolController {

    private final SchoolService service;
    private final ActivityLogService activityLogService;

    public SchoolController(SchoolService service, ActivityLogService activityLogService) {
        this.service = service;
        this.activityLogService = activityLogService;
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
        model.addAttribute("pageTitle", "School Profile");


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
            @RequestParam(value = "signatureFile", required = false) MultipartFile signatureFile,
            RedirectAttributes redirectAttributes) {

        service.save(school, logoFile, signatureFile);

        redirectAttributes.addFlashAttribute("success", "School profile updated successfully.");

        activityLogService.logUpdate("School Profile", "Updated school profile.");

        return "redirect:/settings/school";

    }



}