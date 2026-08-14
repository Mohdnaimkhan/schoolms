package com.naim.school.search;

import com.naim.school.classroom.ClassRoom;
import com.naim.school.classroom.ClassRoomService;
import com.naim.school.fee.Fee;
import com.naim.school.fee.FeeRepository;
import com.naim.school.notice.Notice;
import com.naim.school.notice.NoticeRepository;
import com.naim.school.section.Section;
import com.naim.school.section.SectionService;
import com.naim.school.security.CurrentUserService;
import com.naim.school.security.Role;
import com.naim.school.student.Student;
import com.naim.school.student.StudentService;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherService;
import com.naim.school.teachersession.TeacherSession;
import com.naim.school.teachersession.TeacherSessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class GlobalSearchController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ClassRoomService classRoomService;
    private final SectionService sectionService;
    private final FeeRepository feeRepository;
    private final NoticeRepository noticeRepository;
    private final CurrentUserService currentUserService;
    private final TeacherSessionRepo teacherSessionRepo;
    private final StudentSessionRepo studentSessionRepo;

    @GetMapping(value = "/global", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalSearchResponse search(@RequestParam(defaultValue = "") String q) {
        String query = normalize(q);
        if (query.length() < 2) return emptyResponse();

        Role role = currentUserService.getCurrentUser().getRole();
        Teacher linkedTeacher = currentUserService.getCurrentUser().getTeacher();

        Set<Long> allowedStudentIds = new HashSet<>();
        Set<Long> allowedClassIds = new HashSet<>();
        Set<Long> allowedSectionIds = new HashSet<>();

        if (role == Role.TEACHER) {
            if (linkedTeacher == null) return emptyResponse();
            for (TeacherSession assignment : teacherSessionRepo.findByTeacherOrderByAcademicSession_IdDesc(linkedTeacher)) {
                if (!Boolean.TRUE.equals(assignment.getCurrentSession())) continue;
                if (assignment.getClassRoom() != null) allowedClassIds.add(assignment.getClassRoom().getId());
                if (assignment.getSection() != null) allowedSectionIds.add(assignment.getSection().getId());
            }
            for (StudentSession ss : studentSessionRepo.findByCurrentSessionTrue()) {
                boolean classMatch = ss.getClassRoom() != null && allowedClassIds.contains(ss.getClassRoom().getId());
                boolean sectionMatch = ss.getSection() != null && allowedSectionIds.contains(ss.getSection().getId());
                if (classMatch || sectionMatch) allowedStudentIds.add(ss.getStudent().getId());
            }
        }

        List<SearchItem> students = new ArrayList<>();
        for (Student student : studentService.getAllStudents()) {
            if (role == Role.TEACHER && !allowedStudentIds.contains(student.getId())) continue;
            if (matches(query, student.getStudentName(), student.getAdmissionNo(), student.getMobileNumber(),
                    student.getEmergencyContact(), student.getAadhaarNumber(), student.getPenNo(),
                    student.getApaarId(), student.getFatherName())) {
                students.add(new SearchItem("STUDENTS", safe(student.getStudentName()),
                        "Admission: " + safe(student.getAdmissionNo()) + " • " + safe(student.getFormattedMobileNumber()),
                        "/students/view/" + student.getId(), "person-badge"));
            }
        }

        List<SearchItem> teachers = new ArrayList<>();
        if (role == Role.ADMIN || role == Role.STAFF) {
            for (Teacher teacher : teacherService.getAllTeachers()) {
                if (matches(query, teacher.getTeacherName(), teacher.getEmployeeCode(), teacher.getTenNo(), teacher.getMobile(), teacher.getEmail())) {
                    teachers.add(new SearchItem("TEACHERS", safe(teacher.getTeacherName()),
                            join(teacher.getSubject() != null ? teacher.getSubject().getSubjectName() : "Teacher", " • ", formatMobile(teacher.getMobile())),
                            "/teachers/edit/" + teacher.getId(), "person-workspace"));
                }
            }
        } else if (linkedTeacher != null && matches(query, linkedTeacher.getTeacherName(), linkedTeacher.getEmployeeCode(), linkedTeacher.getTenNo(), linkedTeacher.getMobile(), linkedTeacher.getEmail())) {
            teachers.add(new SearchItem("TEACHERS", safe(linkedTeacher.getTeacherName()), "My Teacher Profile",
                    "/teachers/edit/" + linkedTeacher.getId(), "person-workspace"));
        }

        List<SearchItem> classes = new ArrayList<>();
        if (role != Role.TEACHER) for (ClassRoom classroom : classRoomService.getAllClassRooms()) {
            if (matches(query, classroom.getClassName(), classroom.getDescription())) {
                classes.add(new SearchItem("CLASSES", safe(classroom.getClassName()), "Class",
                        "/classrooms/edit/" + classroom.getId(), "building"));
            }
        }

        List<SearchItem> sections = new ArrayList<>();
        if (role != Role.TEACHER) for (Section section : sectionService.getAllSections()) {
            if (matches(query, section.getSectionName(), section.getDescription())) {
                sections.add(new SearchItem("SECTIONS", safe(section.getSectionName()), "Section",
                        "/sections/edit/" + section.getId(), "diagram-3"));
            }
        }

        List<SearchItem> other = new ArrayList<>();
        if (role == Role.ADMIN || role == Role.STAFF) {
            for (Fee fee : feeRepository.findAll()) {
                StudentSession session = fee.getStudentSession();
                Student student = session != null ? session.getStudent() : null;
                String studentName = student != null ? student.getStudentName() : "Student";
                String admissionNo = student != null ? student.getAdmissionNo() : "";
                String mobile = student != null ? student.getMobileNumber() : "";
                String feeHead = fee.getFeeHead() != null ? fee.getFeeHead().getName() : "Fee";
                if (matches(query, fee.getReceiptNo(), studentName, admissionNo, mobile, feeHead)) {
                    other.add(new SearchItem("FEES", "Receipt #" + safe(fee.getReceiptNo()),
                            studentName + " • ₹" + (fee.getPaidAmount() == null ? "0" : fee.getPaidAmount()),
                            "/fees/edit/" + fee.getId(), "receipt"));
                }
                if (other.size() >= 8) break;
            }
        }

        for (Notice notice : noticeRepository.findAllByOrderByNoticeDateDesc()) {
            if (matches(query, notice.getTitle(), notice.getContent())) {
                other.add(new SearchItem("NOTICES", safe(notice.getTitle()),
                        notice.getNoticeDate() != null ? notice.getNoticeDate().toString() : "Notice",
                        "/notices/edit/" + notice.getId(), "megaphone"));
            }
            if (other.size() >= 12) break;
        }

        return new GlobalSearchResponse(limit(students), limit(teachers), limit(classes), limit(sections), limit(other));
    }

    private GlobalSearchResponse emptyResponse() { return new GlobalSearchResponse(List.of(), List.of(), List.of(), List.of(), List.of()); }

    private List<SearchItem> limit(List<SearchItem> items) {
        return items.stream().sorted(Comparator.comparing(SearchItem::title, String.CASE_INSENSITIVE_ORDER)).limit(6).toList();
    }

    private String normalize(String value) { return value == null ? "" : value.replaceAll("[\\s-]", "").toLowerCase(Locale.ROOT).trim(); }

    private boolean matches(String query, String... values) {
        for (String value : values) if (value != null && normalize(value).contains(query)) return true;
        return false;
    }

    private String safe(String value) { return value == null || value.isBlank() ? "-" : value; }

    private String join(Object... parts) {
        StringBuilder builder = new StringBuilder();
        for (Object part : parts) {
            if (part == null || part.toString().isBlank()) continue;
            if (builder.length() > 0) builder.append("");
            builder.append(part);
        }
        return builder.toString();
    }

    private String formatMobile(String mobile) {
        if (mobile == null) return "";
        String digits = mobile.replaceAll("\\s+", "");
        return digits.length() == 10 ? digits.substring(0, 5) + " " + digits.substring(5) : mobile;
    }

    public record GlobalSearchResponse(List<SearchItem> students, List<SearchItem> teachers, List<SearchItem> classes, List<SearchItem> sections, List<SearchItem> others) {}
    public record SearchItem(String category, String title, String subtitle, String url, String icon) {}
}
