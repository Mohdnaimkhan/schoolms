package com.naim.school.fee;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class dateHome {
@GetMapping("/date")
public String home(Model model) {
    model.addAttribute("date", LocalDate.of(2026, 7, 24));
    return "dateindex";
}

}