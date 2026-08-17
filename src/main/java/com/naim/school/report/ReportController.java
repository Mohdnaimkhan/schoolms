package com.naim.school.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionService;
import com.naim.school.attendance.Attendance;
import com.naim.school.attendance.AttendanceService;
import com.naim.school.attendance.AttendanceStatus;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.exam.Exam;
import com.naim.school.exam.ExamService;
import com.naim.school.exam.ExamSubject;
import com.naim.school.fee.Fee;
import com.naim.school.fee.FeeService;
import com.naim.school.result.Result;
import com.naim.school.result.ResultRepository;
import com.naim.school.section.SectionService;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final StudentSessionService studentSessionService;
    private final AcademicSessionService academicSessionService;
    private final ClassRoomService classRoomService;
    private final SectionService sectionService;
    private final AttendanceService attendanceService;
    private final FeeService feeService;
    private final ExamService examService;
    private final ResultRepository resultRepository;

    // ===========================
    // Landing Page
    // ===========================

    @GetMapping
    public String index(Model model) {

        model.addAttribute("pageTitle", "Reports");

        return "report/index";

    }

    // ===========================
    // Student Report
    // ===========================

    @GetMapping("/students")
    public String studentReport(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long classRoomId,
            @RequestParam(required = false) Long sectionId,
            Model model) {

        model.addAttribute("pageTitle", "Student Report");

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("classRooms", classRoomService.getAllClassRooms());
        model.addAttribute("sections", sectionService.getAllSections());

        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("selectedClassRoomId", classRoomId);
        model.addAttribute("selectedSectionId", sectionId);

        if (sessionId != null) {

            List<StudentSession> sessions = studentSessionService.getByAcademicSession(sessionId);

            if (classRoomId != null) {

                sessions = sessions.stream()
                        .filter(s -> s.getClassRoom() != null && s.getClassRoom().getId().equals(classRoomId))
                        .toList();

            }

            if (sectionId != null) {

                sessions = sessions.stream()
                        .filter(s -> s.getSection() != null && s.getSection().getId().equals(sectionId))
                        .toList();

            }

            model.addAttribute("sessions", sessions);

            Map<String, Long> statusCounts = new LinkedHashMap<>();

            for (StudentSession s : sessions) {

                String key = s.getStatus() != null ? s.getStatus().name() : "UNKNOWN";

                statusCounts.merge(key, 1L, Long::sum);

            }

            model.addAttribute("statusCounts", statusCounts);
            model.addAttribute("totalCount", sessions.size());

        }

        return "report/students";

    }

    // ===========================
    // Attendance Report
    // ===========================

    @GetMapping("/attendance")
    public String attendanceReport(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long classRoomId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Model model) {

        model.addAttribute("pageTitle", "Attendance Report");

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());
        model.addAttribute("classRooms", classRoomService.getAllClassRooms());

        model.addAttribute("selectedSessionId", sessionId);
        model.addAttribute("selectedClassRoomId", classRoomId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        if (sessionId != null) {

            AcademicSession session = academicSessionService.getById(sessionId);

            List<Attendance> records = attendanceService.findByAcademicSession(session);

            if (classRoomId != null) {

                records = records.stream()
                        .filter(a -> a.getClassroom() != null && a.getClassroom().getId().equals(classRoomId))
                        .toList();

            }

            if (fromDate != null) {

                records = records.stream()
                        .filter(a -> a.getAttendanceDate() != null && !a.getAttendanceDate().isBefore(fromDate))
                        .toList();

            }

            if (toDate != null) {

                records = records.stream()
                        .filter(a -> a.getAttendanceDate() != null && !a.getAttendanceDate().isAfter(toDate))
                        .toList();

            }

            Map<Long, AttendanceSummaryRow> summaryByStudentId = new LinkedHashMap<>();

            for (Attendance a : records) {

                if (a.getStudent() == null) {

                    continue;

                }

                Long studentId = a.getStudent().getId();

                AttendanceSummaryRow row = summaryByStudentId.computeIfAbsent(studentId, id -> {

                    AttendanceSummaryRow r = new AttendanceSummaryRow();

                    r.setStudentName(a.getStudent().getStudentName());
                    r.setAdmissionNo(a.getStudent().getAdmissionNo());

                    return r;

                });

                if (a.getStatus() == AttendanceStatus.PRESENT) {

                    row.setPresent(row.getPresent() + 1);

                } else if (a.getStatus() == AttendanceStatus.ABSENT) {

                    row.setAbsent(row.getAbsent() + 1);

                } else if (a.getStatus() == AttendanceStatus.LEAVE) {

                    row.setLeave(row.getLeave() + 1);

                }

            }

            model.addAttribute("summaryRows", new ArrayList<>(summaryByStudentId.values()));

        }

        return "report/attendance";

    }

    // ===========================
    // Fee Report
    // ===========================

    @GetMapping("/fees")
    public String feeReport(
            @RequestParam(required = false) Long sessionId,
            Model model) {

        model.addAttribute("pageTitle", "Fee Report");

        model.addAttribute("academicSessions", academicSessionService.getAllSessions());

        model.addAttribute("selectedSessionId", sessionId);

        if (sessionId != null) {

            AcademicSession session = academicSessionService.getById(sessionId);

            List<Fee> fees = feeService.findByAcademicSession(session);

            model.addAttribute("fees", fees);

            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalPaid = BigDecimal.ZERO;
            BigDecimal totalDue = BigDecimal.ZERO;

            Map<String, BigDecimal> collectionByFeeHead = new LinkedHashMap<>();

            for (Fee fee : fees) {

                if (fee.getAmount() != null) {
                    totalAmount = totalAmount.add(fee.getAmount());
                }

                if (fee.getPaidAmount() != null) {
                    totalPaid = totalPaid.add(fee.getPaidAmount());
                }

                if (fee.getDueAmount() != null) {
                    totalDue = totalDue.add(fee.getDueAmount());
                }

                String headName = fee.getFeeHead() != null ? fee.getFeeHead().getName() : "Unspecified";

                collectionByFeeHead.merge(
                        headName,
                        fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO,
                        BigDecimal::add);

            }

            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("totalPaid", totalPaid);
            model.addAttribute("totalDue", totalDue);
            model.addAttribute("collectionByFeeHead", collectionByFeeHead);

        }

        return "report/fees";

    }

    // ===========================
    // Examination Report
    // ===========================

    @GetMapping("/exams")
    public String examReport(
            @RequestParam(required = false) Long examId,
            Model model) {

        model.addAttribute("pageTitle", "Examination Report");

        model.addAttribute("exams", examService.getAll());

        model.addAttribute("selectedExamId", examId);

        if (examId != null) {

            Exam exam = examService.getById(examId);

            model.addAttribute("exam", exam);

            List<ExamSubject> examSubjects = examService.getSubjects(examId);

            List<SubjectResultSummary> summaries = new ArrayList<>();

            for (ExamSubject es : examSubjects) {

                List<Result> results = resultRepository.findByExamSubject(es);

                SubjectResultSummary summary = new SubjectResultSummary();

                summary.setSubjectName(es.getSubject() != null ? es.getSubject().getSubjectName() : "--");
                summary.setMaxMarks(es.getMaxMarks() != null ? es.getMaxMarks() : 0);
                summary.setStudentsAppeared(results.size());

                BigDecimal total = BigDecimal.ZERO;
                long passCount = 0;

                for (Result r : results) {

                    if (r.getMarksObtained() != null) {

                        total = total.add(r.getMarksObtained());

                    }

                    if (r.isPassed()) {

                        passCount++;

                    }

                }

                summary.setPassCount(passCount);
                summary.setFailCount(results.size() - passCount);

                summary.setAverageMarks(
                        results.isEmpty()
                                ? BigDecimal.ZERO
                                : total.divide(BigDecimal.valueOf(results.size()), 2, RoundingMode.HALF_UP));

                summaries.add(summary);

            }

            model.addAttribute("summaries", summaries);

        }

        return "report/exams";

    }

}
