package com.naim.school.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    /*
     * ==========================================================
     * STUDENT LIST
     * ==========================================================
     */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("totalStudents", studentService.count());
        model.addAttribute("activeStudents", studentService.countActiveStudents());
        model.addAttribute("boys", studentService.countBoys());
        model.addAttribute("girls", studentService.countGirls());

        return "student/list";
    }

    /*
     * ==========================================================
     * ADD FORM
     * ==========================================================
     */

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Add Student");

        loadFormData(model);

        return "student/form";
    }

    /*
     * ==========================================================
     * EDIT FORM
     * ==========================================================
     */

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        model.addAttribute(
                "pageTitle",
                "Edit Student");

        loadFormData(model);

        return "student/form";
    }

    /*
     * ==========================================================
     * SAVE
     * ==========================================================
     */

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "pageTitle",
                    student.getId() == null
                            ? "Add Student"
                            : "Edit Student");

            loadFormData(model);

            return "student/form";
        }

        try {

            studentService.save(student, photoFile);

        } catch (IllegalArgumentException ex) {

            model.addAttribute("error", ex.getMessage());

            model.addAttribute(
                    "pageTitle",
                    student.getId() == null
                            ? "Add Student"
                            : "Edit Student");

            loadFormData(model);

            return "student/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Student saved successfully.");

        return "redirect:/students";
    }

    /*
     * ==========================================================
     * VIEW
     * ==========================================================
     */

    @GetMapping("/view/{id}")
    public String view(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        return "student/view";
    }

    /*
     * ==========================================================
     * PRINT
     * ==========================================================
     */

    @GetMapping("/print/{id}")
    public String print(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        return "student/print";
    }

    /*
     * ==========================================================
     * CHANGE STATUS
     * ==========================================================
     */

    @PostMapping("/status/{id}")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam StudentStatus status,
            RedirectAttributes redirectAttributes) {

        studentService.changeStatus(id, status);

        redirectAttributes.addFlashAttribute(
                "success",
                "Student status updated successfully.");

        return "redirect:/students";
    }

    /*
     * ==========================================================
     * AADHAAR DUPLICATE CHECK
     * ==========================================================
     */

    @GetMapping("/check/aadhaar")
    @ResponseBody
    public boolean checkAadhaar(
            @RequestParam String value,
            @RequestParam(required = false) Long id) {

        return studentService.existsAadhaar(
                value,
                id);
    }

    @GetMapping("/id-card/{id}")
    public String idCard(@PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        return "student/id-card";

    }
    /*
     * ==========================================================
     * LOAD FORM DATA
     * ==========================================================
     */

    private void loadFormData(Model model) {

        model.addAttribute("genders", Gender.values());
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("religions", Religion.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("studentStatuses", StudentStatus.values());

    }

}