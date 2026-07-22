package com.naim.school.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AcademicSessionService academicSessionService;
    private final ClassRoomService classRoomService;

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
     * ADD + EDIT FORM
     * ==========================================================
     */

    @GetMapping("/add")
    public String form(@RequestParam(required = false) Long id,
                       Model model) {

        Student student = (id == null)
                ? new Student()
                : studentService.getById(id);

        model.addAttribute("student", student);

        model.addAttribute("religions", Religion.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("bloodGroups", BloodGroup.values());

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

            model.addAttribute("religions", Religion.values());
            model.addAttribute("categories", Category.values());
            model.addAttribute("bloodGroups", BloodGroup.values());

            loadFormData(model);

            return "student/form";
        }

        studentService.save(student, photoFile);

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
    public String view(@PathVariable Long id,
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
    public String print(@PathVariable Long id,
                        Model model) {

        model.addAttribute(
                "student",
                studentService.getById(id));

        return "student/print";
    }

    /*
     * ==========================================================
     * DELETE
     * ==========================================================
     */

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        studentService.delete(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Student deleted successfully.");

        return "redirect:/students";
    }

    /*
     * ==========================================================
     * DUPLICATE CHECK
     * ==========================================================
     */

    @GetMapping("/check/mobile")
    @ResponseBody
    public boolean checkMobile(
            @RequestParam String value,
            @RequestParam(required = false) Long id) {

        return studentService.existsMobile(value, id);
    }

    @GetMapping("/check/aadhaar")
    @ResponseBody
    public boolean checkAadhaar(
            @RequestParam String value,
            @RequestParam(required = false) Long id) {

        return studentService.existsAadhaar(value, id);
    }

    @GetMapping("/check/email")
    @ResponseBody
    public boolean checkEmail(
            @RequestParam String value,
            @RequestParam(required = false) Long id) {

        return studentService.existsEmail(value, id);
    }

    /*
     * ==========================================================
     * LOAD FORM DATA
     * ==========================================================
     */

    private void loadFormData(Model model) {

        model.addAttribute(
                "academicSessions",
                academicSessionService.getAllSessions());

        model.addAttribute(
                "classRooms",
                classRoomService.getAllClasses());

        model.addAttribute(
                "genders",
                Gender.values());

        model.addAttribute(
                "studentStatuses",
                StudentStatus.values());

        model.addAttribute(
                "admissionTypes",
                AdmissionType.values());
    }

}