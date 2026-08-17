package com.naim.school.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/setup")
public class SetupController {

    private final UserService userService;

    @GetMapping
    public String setup(Model model) {
        if (userService.adminExists()) {
            return "redirect:/login";
        }
        model.addAttribute("pageTitle", "Administrator Setup");
        return "setup/admin";
    }

    @PostMapping
    public String createAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (userService.adminExists()) {
            return "redirect:/login";
        }

        try {
            userService.createInitialAdmin(username, password, confirmPassword);
            return "redirect:/login?setup=success";
        } catch (RuntimeException ex) {
            model.addAttribute("pageTitle", "Administrator Setup");
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("username", username);
            return "setup/admin";
        }
    }
}
