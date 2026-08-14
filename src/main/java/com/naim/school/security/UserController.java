package com.naim.school.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.naim.school.teacher.TeacherService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TeacherService teacherService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "User Accounts");

        java.util.List<User> users = userService.getAll();
        long userActive = users.stream().filter(u -> Boolean.TRUE.equals(u.getActive())).count();
        long userAdmins = users.stream().filter(u -> u.getRole() == Role.ADMIN).count();
        long userStaff = users.stream().filter(u -> u.getRole() == Role.STAFF).count();
        long userTeachers = users.stream().filter(u -> u.getRole() == Role.TEACHER).count();
        model.addAttribute("users", users);
        model.addAttribute("userTotal", users.size());
        model.addAttribute("userActive", userActive);
        model.addAttribute("userAdmins", userAdmins);
        model.addAttribute("userStaff", userStaff);
        model.addAttribute("userTeachers", userTeachers);

        return "users/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add User");

        model.addAttribute("user", new User());

        model.addAttribute("roles", Role.values());
        model.addAttribute("teachers", teacherService.getActiveTeachers());
        model.addAttribute("teachers", teacherService.getActiveTeachers());

        return "users/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit User");

        User user = userService.getById(id);
        if (user.getTeacher() != null) {
            user.setTeacherId(user.getTeacher().getId());
        }
        model.addAttribute("user", user);

        model.addAttribute("roles", Role.values());

        return "users/form";

    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute User user,
            @RequestParam(value = "newPassword", required = false) String newPassword) {

        userService.save(user, newPassword);

        return "redirect:/users";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        userService.delete(id);

        return "redirect:/users";

    }

}
