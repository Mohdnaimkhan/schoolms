package com.naim.school.dashboard;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard(HttpServletRequest request, Model model) {

        model.addAttribute("currentPath", request.getRequestURI());

        return "dashboard/index";

    }

}