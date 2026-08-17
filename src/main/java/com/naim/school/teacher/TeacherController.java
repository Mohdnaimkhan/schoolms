package com.naim.school.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.subject.SubjectService;
import com.naim.school.activitylog.ActivityLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final ActivityLogService activityLogService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Teachers");
        java.util.List<Teacher> teachers = teacherService.getAllTeachers();
        long teacherActive = teachers.stream().filter(t -> Boolean.TRUE.equals(t.getActive())).count();
        long teacherMale = teachers.stream().filter(t -> "Male".equalsIgnoreCase(t.getGender())).count();
        long teacherFemale = teachers.stream().filter(t -> "Female".equalsIgnoreCase(t.getGender())).count();

        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherTotal", teachers.size());
        model.addAttribute("teacherActive", teacherActive);
        model.addAttribute("teacherMale", teacherMale);
        model.addAttribute("teacherFemale", teacherFemale);

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
                       @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("subjects", subjectService.getAllSubjects());

            return "teacher/form";

        }

        boolean isNew = teacher.getId() == null;
        teacherService.save(teacher, photoFile);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Teacher added successfully." : "Teacher updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Teacher", "Added teacher " + teacher.getTeacherName()
                    + " (" + teacher.getEmployeeCode() + ")");
        } else {
            activityLogService.logUpdate("Teacher", "Updated teacher " + teacher.getTeacherName()
                    + " (" + teacher.getEmployeeCode() + ")");
        }

        return "redirect:/teachers";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Teacher teacher = teacherService.getById(id);
        teacherService.delete(id);

        redirectAttributes.addFlashAttribute("success", "Teacher deleted successfully.");

        activityLogService.logDelete("Teacher", "Deleted teacher " + teacher.getTeacherName()
                + " (" + teacher.getEmployeeCode() + ")");

        return "redirect:/teachers";

    }

}