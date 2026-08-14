package com.naim.school.feestructure;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.feehead.FeeHeadService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fee-structures")
public class FeeStructureController {

    private final FeeStructureService feeStructureService;

    private final AcademicSessionService academicSessionService;

    private final ClassRoomService classRoomService;

    private final FeeHeadService feeHeadService;

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Fee Structures");

        List<FeeStructure> feeStructures = sessionId == null
                ? feeStructureService.getAll()
                : feeStructureService.getByAcademicSession(sessionId);
        BigDecimal feeStructureAmount = feeStructures.stream().map(FeeStructure::getAmount).filter(a -> a != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        long feeStructureSessions = feeStructures.stream().filter(f -> f.getAcademicSession() != null).map(f -> f.getAcademicSession().getId()).distinct().count();
        long feeStructureClasses = feeStructures.stream().filter(f -> f.getClassRoom() != null).map(f -> f.getClassRoom().getId()).distinct().count();
        model.addAttribute("feeStructures", feeStructures);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("feeStructureTotal", feeStructures.size());
        model.addAttribute("feeStructureAmount", feeStructureAmount);
        model.addAttribute("feeStructureSessions", feeStructureSessions);
        model.addAttribute("feeStructureClasses", feeStructureClasses);

        return "feestructure/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Fee Structure");

        model.addAttribute("feeStructure", new FeeStructure());

        loadMasters(model);

        return "feestructure/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Fee Structure");

        model.addAttribute("feeStructure", feeStructureService.getById(id));

        loadMasters(model);

        return "feestructure/form";

    }

    @PostMapping("/save")
    public String save(@ModelAttribute FeeStructure feeStructure) {

        feeStructureService.save(feeStructure);

        return "redirect:/fee-structures";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        feeStructureService.delete(id);

        return "redirect:/fee-structures";

    }

    private void loadMasters(Model model) {

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

        model.addAttribute("feeHeads", feeHeadService.getAllFeeHeads());

    }

}
