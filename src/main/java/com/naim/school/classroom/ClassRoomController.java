package com.naim.school.classroom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classrooms")
public class ClassRoomController {

    private final ClassRoomService service;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Class Rooms");
        model.addAttribute("classRooms", service.getAllClassRooms());

        return "classroom/list";

    }

    /*
     * ==========================================
     * ADD
     * ==========================================
     */

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Class");
        model.addAttribute("classRoom", new ClassRoom());

        return "classroom/form";

    }

    /*
     * ==========================================
     * EDIT
     * ==========================================
     */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        model.addAttribute("pageTitle", "Edit Class");
        model.addAttribute("classRoom", service.getById(id));

        return "classroom/form";

    }

    /*
     * ==========================================
     * SAVE
     * ==========================================
     */

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute ClassRoom classRoom) {

        service.save(classRoom);

        return "redirect:/classrooms";

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id) {

        service.changeStatus(id);

        return "redirect:/classrooms";

    }

}