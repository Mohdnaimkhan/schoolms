package com.naim.school.sms;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SchoolSettingController {

    private final SchoolSettingService service;

    @GetMapping
    public String index(Model model) {

        SchoolSetting setting = service.getSetting();

        if (setting == null) {
            setting = new SchoolSetting();
        }

        model.addAttribute("setting", setting);

        return "school/settings";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("setting") SchoolSetting setting,
                       BindingResult result) {

        if (result.hasErrors()) {
            return "school/settings";
        }

        if (setting.getId() == null) {
            service.save(setting);
        } else {
            service.update(setting.getId(), setting);
        }

        return "redirect:/settings";
    }

}