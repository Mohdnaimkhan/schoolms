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
<<<<<<< HEAD
        java.util.List<ClassRoom> classRooms = service.getAllClassRooms();
        long classActive = classRooms.stream().filter(c -> Boolean.TRUE.equals(c.getActive())).count();
        long classConfigured = classRooms.stream().filter(c -> c.getDescription() != null && !c.getDescription().isBlank()).count();
        model.addAttribute("classRooms", classRooms);
        model.addAttribute("classTotal", classRooms.size());
        model.addAttribute("classActive", classActive);
        model.addAttribute("classInactive", classRooms.size() - classActive);
        model.addAttribute("classConfigured", classConfigured);
=======
        model.addAttribute("classRooms", service.getAllClassRooms());
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

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
<<<<<<< HEAD
    public String save(@Valid @ModelAttribute ClassRoom classRoom, BindingResult result) {

        if (result.hasErrors()) {

            return "classroom/form";

        }
=======
    public String save(@Valid @ModelAttribute ClassRoom classRoom) {
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

        service.save(classRoom);

        return "redirect:/classrooms";

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

<<<<<<< HEAD
    @PostMapping("/status/{id}")
=======
    @GetMapping("/status/{id}")
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    public String changeStatus(@PathVariable Long id) {

        service.changeStatus(id);

        return "redirect:/classrooms";

    }

}