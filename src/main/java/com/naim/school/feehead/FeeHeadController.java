package com.naim.school.feehead;

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
@RequestMapping("/fee-heads")
public class FeeHeadController {

    private final FeeHeadService service;
    private final ActivityLogService activityLogService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Fee Heads");
        java.util.List<FeeHead> feeHeads = service.getAllFeeHeads();
        long feeHeadActive = feeHeads.stream().filter(f -> Boolean.TRUE.equals(f.getActive())).count();
        long feeHeadConfigured = feeHeads.stream().filter(f -> f.getDescription() != null && !f.getDescription().isBlank()).count();
        model.addAttribute("feeHeads", feeHeads);
        model.addAttribute("feeHeadTotal", feeHeads.size());
        model.addAttribute("feeHeadActive", feeHeadActive);
        model.addAttribute("feeHeadInactive", feeHeads.size() - feeHeadActive);
        model.addAttribute("feeHeadConfigured", feeHeadConfigured);

        return "feehead/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Fee Head");
        model.addAttribute("feeHead", new FeeHead());

        return "feehead/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Fee Head");
        model.addAttribute("feeHead", service.getById(id));

        return "feehead/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute FeeHead feeHead, BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            return "feehead/form";

        }

        boolean isNew = feeHead.getId() == null;
        service.save(feeHead);

        redirectAttributes.addFlashAttribute("success",
                isNew ? "Fee head added successfully." : "Fee head updated successfully.");

        if (isNew) {
            activityLogService.logCreate("Fee Head", "Added fee head " + feeHead.getName());
        } else {
            activityLogService.logUpdate("Fee Head", "Updated fee head " + feeHead.getName());
        }

        return "redirect:/fee-heads";

    }

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        service.changeStatus(id);

        redirectAttributes.addFlashAttribute("success", "Fee head status updated successfully.");

        activityLogService.logStatusChange("Fee Head", "Changed status for fee head #" + id);

        return "redirect:/fee-heads";

    }

}
