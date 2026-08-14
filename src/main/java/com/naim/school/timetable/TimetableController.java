package com.naim.school.timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.section.SectionService;
import com.naim.school.subject.SubjectService;
import com.naim.school.teacher.TeacherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    private final AcademicSessionService academicSessionService;

    private final ClassRoomService classRoomService;

    private final SectionService sectionService;

    private final SubjectService subjectService;

    private final TeacherService teacherService;

    // ===========================
    // List
    // ===========================

    @GetMapping
    public String list(
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Timetable");

        List<Timetable> timetables = sessionId == null
                ? timetableService.getAll()
                : timetableService.getByAcademicSession(sessionId);
        long timetableConfigured = timetables.stream().filter(t -> t.getTeacher() != null && t.getSubject() != null).count();
        long timetableSections = timetables.stream().filter(t -> t.getSection() != null).count();
        String todayName = java.time.LocalDate.now().getDayOfWeek().name();
        long timetableToday = timetables.stream().filter(t -> t.getWeekDay() != null && t.getWeekDay().name().equalsIgnoreCase(todayName)).count();
        model.addAttribute("timetables", timetables);
        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("timetableTotal", timetables.size());
        model.addAttribute("timetableConfigured", timetableConfigured);
        model.addAttribute("timetableSections", timetableSections);
        model.addAttribute("timetableToday", timetableToday);

        return "timetable/list";

    }

    // ===========================
    // Add
    // ===========================

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Timetable Entry");

        model.addAttribute("timetable", new Timetable());

        model.addAttribute("weekDays", WeekDay.values());

        loadMasters(model);

        return "timetable/form";

    }

    // ===========================
    // Edit
    // ===========================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Timetable Entry");

        model.addAttribute("timetable", timetableService.getById(id));

        model.addAttribute("weekDays", WeekDay.values());

        loadMasters(model);

        return "timetable/form";

    }

    // ===========================
    // Save
    // ===========================

    @PostMapping("/save")
    public String save(@ModelAttribute Timetable timetable) {

        timetableService.save(timetable);

        return "redirect:/timetable";

    }

    // ===========================
    // Delete
    // ===========================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        timetableService.delete(id);

        return "redirect:/timetable";

    }

    // ===========================
    // Weekly Grid View
    // ===========================

    @GetMapping("/view")
    public String weeklyView(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long classRoomId,
            @RequestParam(required = false) Long sectionId,
            Model model) {

        model.addAttribute("pageTitle", "Weekly Timetable");

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

        model.addAttribute("sections", sectionService.getAllSections());

        model.addAttribute("weekDays", WeekDay.values());

        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("selectedClassRoomId", classRoomId);
        model.addAttribute("selectedSectionId", sectionId);

        if (sessionId != null && classRoomId != null) {

            List<Timetable> entries = timetableService.getWeeklyGrid(sessionId, classRoomId, sectionId);

            model.addAttribute("entries", entries);

            int maxPeriod = entries.stream()
                    .map(Timetable::getPeriodNumber)
                    .filter(p -> p != null)
                    .max(Integer::compareTo)
                    .orElse(8);

            maxPeriod = Math.max(maxPeriod, 8);

            model.addAttribute("maxPeriod", maxPeriod);

            // Build period -> day -> entry lookup so the template only needs
            // simple map access, rather than filtering the flat list itself.
            Map<Integer, Map<String, Timetable>> grid = new TreeMap<>();

            for (int period = 1; period <= maxPeriod; period++) {

                grid.put(period, new HashMap<>());

            }

            for (Timetable entry : entries) {

                if (entry.getPeriodNumber() == null || entry.getWeekDay() == null) {

                    continue;

                }

                grid.computeIfAbsent(entry.getPeriodNumber(), p -> new HashMap<>())
                        .put(entry.getWeekDay().name(), entry);

            }

            model.addAttribute("grid", grid);

        }

        return "timetable/weekly-view";

    }

    private void loadMasters(Model model) {

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

        model.addAttribute("sections", sectionService.getAllSections());

        model.addAttribute("subjects", subjectService.getAllSubjects());

        model.addAttribute("teachers", teacherService.getAllTeachers());

    }

}
