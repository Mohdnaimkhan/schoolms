package com.naim.school.activitylog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/activity-log")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    private static final List<String> MODULES = List.of(
            "Student", "Teacher", "Academic Session", "Attendance", "Class Room", "Section",
            "Subject", "Fee Head", "Fee Structure", "Fee", "Exam", "Result", "Timetable",
            "Notice", "Expense", "Certificate", "Student Session", "Teacher Session",
            "User", "School Profile", "Salary Ledger", "Salary Payment", "Auth"
    );

    @GetMapping
    public String list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) ActivityAction action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        List<ActivityLog> logs = activityLogService.search(username, module, action, fromDate, toDate);

        model.addAttribute("logs", logs);
        model.addAttribute("modules", MODULES);
        model.addAttribute("actions", ActivityAction.values());
        model.addAttribute("selectedUsername", username);
        model.addAttribute("selectedModule", module);
        model.addAttribute("selectedAction", action);
        model.addAttribute("selectedFromDate", fromDate);
        model.addAttribute("selectedToDate", toDate);
        model.addAttribute("pageTitle", "Activity Log");

        return "activitylog/list";

    }

}
