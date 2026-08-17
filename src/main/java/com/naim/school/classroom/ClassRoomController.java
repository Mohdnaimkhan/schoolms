package com.naim.school.classroom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.naim.school.activitylog.ActivityLogService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classrooms")
public class ClassRoomController {

    private final ClassRoomService service;
    private final ActivityLogService activityLogService;

    /*
     * ==========================================
     * LIST
     * ==========================================
     */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Class Rooms");
        java.util.List<ClassRoom> classRooms = service.getAllClassRooms();
        long classActive = classRooms.stream().filter(c -> Boolean.TRUE.equals(c.getActive())).count();
        long classConfigured = classRooms.stream().filter(c -> c.getDescription() != null && !c.getDescription().isBlank()).count();
        model.addAttribute("classRooms", classRooms);
        model.addAttribute("classTotal", classRooms.size());
        model.addAttribute("classActive", classActive);
        model.addAttribute("classInactive", classRooms.size() - classActive);
        model.addAttribute("classConfigured", classConfigured);

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
    public String save(@Valid @ModelAttribute ClassRoom classRoom, BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return "classroom/form";

        }

        boolean isNew = classRoom.getId() == null;
        service.save(classRoom);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Class added successfully." : "Class updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Class Room", "Added class " + classRoom.getClassName());
        } else {
            activityLogService.logUpdate("Class Room", "Updated class " + classRoom.getClassName());
        }

        return "redirect:/classrooms";

    }

    /*
     * ==========================================
     * CHANGE STATUS
     * ==========================================
     */

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        service.changeStatus(id);

        redirectAttributes.addFlashAttribute("success", "Class status updated successfully.");

        activityLogService.logStatusChange("Class Room", "Changed status for class #" + id);

        return "redirect:/classrooms";

    }

}