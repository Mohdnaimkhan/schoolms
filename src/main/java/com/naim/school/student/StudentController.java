package com.naim.school.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.classroom.ClassRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final ClassRoomService classRoomService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Students");
        model.addAttribute("students", studentService.getAllStudents());

        return "student/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Student");
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classRoomService.getActiveClasses());

        return "student/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Student");
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("classes", classRoomService.getActiveClasses());

        return "student/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Student student,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {

            model.addAttribute("classes", classRoomService.getActiveClasses());

            return "student/form";

        }

        studentService.save(student);

        return "redirect:/students";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        studentService.delete(id);

        return "redirect:/students";

    }

}