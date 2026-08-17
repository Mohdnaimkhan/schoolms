package com.naim.school.notice;

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
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService service;
    private final ActivityLogService activityLogService;

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
    public String save(@Valid @ModelAttribute Notice notice, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("audiences", NoticeAudience.values());

            return "notice/form";

        }

        boolean isNew = notice.getId() == null;
        service.save(notice);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Notice added successfully." : "Notice updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Notice", "Added notice " + notice.getTitle());
        } else {
            activityLogService.logUpdate("Notice", "Updated notice " + notice.getTitle());
        }

        return "redirect:/notices";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Notice notice = service.getById(id);
        service.delete(id);

        redirectAttributes.addFlashAttribute("success", "Notice deleted successfully.");

        activityLogService.logDelete("Notice", "Deleted notice " + notice.getTitle());

        return "redirect:/notices";

    }

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        service.changeStatus(id);

        redirectAttributes.addFlashAttribute("success", "Notice status updated successfully.");

        activityLogService.logStatusChange("Notice", "Changed status for notice #" + id);

        return "redirect:/notices";

    }

}
