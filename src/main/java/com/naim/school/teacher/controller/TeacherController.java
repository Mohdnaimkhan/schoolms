package com.naim.school.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.subject.service.SubjectService;
import com.naim.school.teacher.entity.Teacher;
import com.naim.school.teacher.service.TeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Teachers");
        model.addAttribute("teachers", teacherService.getAllTeachers());

        return "teacher/list";
    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Teacher");
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return "teacher/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Teacher");
        model.addAttribute("teacher", teacherService.getById(id));
        model.addAttribute("subjects", subjectService.getAllSubjects());

        return "teacher/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Teacher teacher,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {

            model.addAttribute("subjects", subjectService.getAllSubjects());

            return "teacher/form";

        }

        teacherService.save(teacher);

        return "redirect:/teachers";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        teacherService.delete(id);

        return "redirect:/teachers";

    }

}