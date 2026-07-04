package com.naim.school.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.sms.entity.SchoolSetting;
import com.naim.school.sms.service.SchoolSettingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SchoolSettingController {

    private final SchoolSettingService schoolSettingService;

    /**
     * Open Settings Page
     */
    @GetMapping("/settings")
    public String settings(Model model) {

        model.addAttribute("pageTitle", "School Settings");
        model.addAttribute("setting", schoolSettingService.getSettings());

        return "school/settings";
    }

    /**
     * Save Settings
     */
    @PostMapping("/settings/save")
    public String saveSettings(
            @ModelAttribute SchoolSetting setting,
            RedirectAttributes redirectAttributes) {

        schoolSettingService.save(setting);

        redirectAttributes.addFlashAttribute(
                "success",
                "School settings saved successfully.");

        return "redirect:/settings";
    }

}
