package com.naim.school.sms;

<<<<<<< HEAD
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.naim.school.notice.Notice;
import com.naim.school.notice.NoticeService;
import com.naim.school.schools.School;
import com.naim.school.schools.SchoolService;
import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionService;
=======
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.naim.school.schools.School;
import com.naim.school.schools.SchoolService;
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SchoolService schoolService;

<<<<<<< HEAD
    private final NoticeService noticeService;

    private final AcademicSessionService academicSessionService;

    public GlobalControllerAdvice(SchoolService schoolService,
                                  NoticeService noticeService,
                                  AcademicSessionService academicSessionService) {
        this.schoolService = schoolService;
        this.noticeService = noticeService;
        this.academicSessionService = academicSessionService;
    }

    @ModelAttribute("appName")
    public String appName() { return AppInfo.APP_NAME; }

    @ModelAttribute("version")
    public String version() { return AppInfo.VERSION; }

    @ModelAttribute("releaseDate")
    public String releaseDate() { return AppInfo.RELEASE_DATE; }

    @ModelAttribute("lastUpdated")
    public String lastUpdated() { return AppInfo.LAST_UPDATED; }

    @ModelAttribute("changeLog")
    public List<AppInfo.ChangeLog> changeLog() { return AppInfo.CHANGELOG; }

=======
    public GlobalControllerAdvice(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    @ModelAttribute("schoolInfo")
    public School schoolInfo() {

        return schoolService.getSchool().orElse(new School());

    }

<<<<<<< HEAD
    @ModelAttribute("currentUsername")
    public String currentUsername(Authentication authentication) {

        return (authentication != null && authentication.isAuthenticated())
                ? authentication.getName()
                : null;

    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {

        if (authentication == null) {

            return false;

        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {

            if ("ROLE_ADMIN".equals(authority.getAuthority())) {

                return true;

            }

        }

        return false;

    }


    @ModelAttribute("isStaff")
    public boolean isStaff(Authentication authentication) {
        return hasAuthority(authentication, "ROLE_STAFF");
    }

    @ModelAttribute("isTeacher")
    public boolean isTeacher(Authentication authentication) {
        return hasAuthority(authentication, "ROLE_TEACHER");
    }

    private boolean hasAuthority(Authentication authentication, String expected) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> expected.equals(a.getAuthority()));
    }

    /*
     * Active notices, available on every page (via the navbar bell icon) so
     * they're impossible to miss - not just tucked away on the dashboard.
     */

    @ModelAttribute("currentAcademicSession")
    public AcademicSession currentAcademicSession() {

        return academicSessionService.getCurrentSessionOrNull();

    }

    @ModelAttribute("navNotices")
    public List<Notice> navNotices() {

        return noticeService.getActive()
                .stream()
                .limit(5)
                .toList();

    }

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
}