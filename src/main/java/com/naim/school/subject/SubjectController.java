package com.naim.school.subject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.classroom.ClassRoomService;
import com.naim.school.activitylog.ActivityLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService service;
    private final ClassRoomService classRoomService;
    private final ActivityLogService activityLogService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Subjects");
        java.util.List<Subject> subjects = service.getAllSubjects();
        long subjectActive = subjects.stream().filter(sub -> Boolean.TRUE.equals(sub.getActive())).count();
        long subjectWithCode = subjects.stream().filter(sub -> sub.getSubjectCode() != null && !sub.getSubjectCode().isBlank()).count();
        model.addAttribute("subjects", subjects);
        model.addAttribute("subjectTotal", subjects.size());
        model.addAttribute("subjectActive", subjectActive);
        model.addAttribute("subjectInactive", subjects.size() - subjectActive);
        model.addAttribute("subjectWithCode", subjectWithCode);

        return "subject/list";
    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Subject");
        model.addAttribute("subject", new Subject());
        model.addAttribute("classes", classRoomService.getAllClassRooms());

        return "subject/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Subject");
        model.addAttribute("subject", service.getById(id));
        model.addAttribute("classes", classRoomService.getAllClassRooms());

        return "subject/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Subject subject,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("classes", classRoomService.getAllClassRooms());

            return "subject/form";
        }

        boolean isNew = subject.getId() == null;
        service.save(subject);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Subject added successfully." : "Subject updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Subject", "Added subject " + subject.getSubjectName());
        } else {
            activityLogService.logUpdate("Subject", "Updated subject " + subject.getSubjectName());
        }

        return "redirect:/subjects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Subject subject = service.getById(id);
        service.delete(id);

        redirectAttributes.addFlashAttribute("success", "Subject deleted successfully.");

        activityLogService.logDelete("Subject", "Deleted subject " + subject.getSubjectName());

        return "redirect:/subjects";
    }

}