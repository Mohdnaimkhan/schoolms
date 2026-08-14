package com.naim.school.notice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService service;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Notices");

        java.util.List<Notice> notices = service.getAll();
        long noticeActive = notices.stream().filter(n -> Boolean.TRUE.equals(n.getActive())).count();
        long noticeToday = notices.stream().filter(n -> n.getNoticeDate() != null && n.getNoticeDate().equals(java.time.LocalDate.now())).count();
        model.addAttribute("notices", notices);
        model.addAttribute("noticeTotal", notices.size());
        model.addAttribute("noticeActive", noticeActive);
        model.addAttribute("noticeInactive", notices.size() - noticeActive);
        model.addAttribute("noticeToday", noticeToday);

        return "notice/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Notice");

        model.addAttribute("notice", new Notice());

        model.addAttribute("audiences", NoticeAudience.values());

        return "notice/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Notice");

        model.addAttribute("notice", service.getById(id));

        model.addAttribute("audiences", NoticeAudience.values());

        return "notice/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Notice notice, BindingResult result, Model model) {

        if (result.hasErrors()) {

            model.addAttribute("audiences", NoticeAudience.values());

            return "notice/form";

        }

        service.save(notice);

        return "redirect:/notices";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/notices";

    }

    @PostMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id) {

        service.changeStatus(id);

        return "redirect:/notices";

    }

}
