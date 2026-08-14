package com.naim.school.feehead;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fee-heads")
public class FeeHeadController {

    private final FeeHeadService service;

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
    public String save(@Valid @ModelAttribute FeeHead feeHead, BindingResult result) {

        if (result.hasErrors()) {

            return "feehead/form";

        }

        service.save(feeHead);

        return "redirect:/fee-heads";

    }

    @PostMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id) {

        service.changeStatus(id);

        return "redirect:/fee-heads";

    }

}
