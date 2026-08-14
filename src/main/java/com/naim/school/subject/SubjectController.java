package com.naim.school.subject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.classroom.ClassRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService service;
    private final ClassRoomService classRoomService;

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
                       Model model) {

        if (result.hasErrors()) {

            model.addAttribute("classes", classRoomService.getAllClassRooms());

            return "subject/form";
        }

        service.save(subject);

        return "redirect:/subjects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/subjects";
    }

}