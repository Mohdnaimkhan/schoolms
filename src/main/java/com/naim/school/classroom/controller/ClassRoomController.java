package com.naim.school.classroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.naim.school.classroom.entity.ClassRoom;
import com.naim.school.classroom.service.ClassRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classes")
public class ClassRoomController {

    private final ClassRoomService service;

    /**
     * Class List
     */
    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Class Room");

        model.addAttribute("classes", service.getAllClasses());

        return "classroom/list";
    }

    /**
     * Add Form
     */
    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Class");

        model.addAttribute("classRoom", new ClassRoom());

        return "classroom/form";
    }

    /**
     * Edit Form
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Class");

        model.addAttribute("classRoom", service.getById(id));

        return "classroom/form";
    }

    /**
     * Save / Update
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("classRoom") ClassRoom classRoom,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {

            model.addAttribute("pageTitle",
                    classRoom.getId() == null
                            ? "Add Class"
                            : "Edit Class");

            return "classroom/form";
        }

        service.save(classRoom);

        return "redirect:/classes";
    }

    /**
     * Delete
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/classes";
    }

}