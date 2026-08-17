package com.naim.school.activitylog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository repository;

    /**
     * Records one activity log entry for the currently logged-in user.
     * Never throws - a logging failure should never break the action the
     * user was actually trying to perform, so any error here is caught
     * and logged instead of propagated.
     */
    public void log(ActivityAction action, String module, String description) {

        try {

            ActivityLog entry = new ActivityLog(currentUsername(), action, module, description);

            repository.save(entry);

        } catch (Exception e) {

            log.warn("Failed to record activity log entry [{} {} - {}]", action, module, description, e);

        }

    }

    /**
     * Same as {@link #log}, but for events (e.g. login) where there is no
     * authenticated principal yet to read from the security context.
     */
    public void logAs(String username, ActivityAction action, String module, String description) {

        try {

            repository.save(new ActivityLog(username, action, module, description));

        } catch (Exception e) {

            log.warn("Failed to record activity log entry [{} {} - {}]", action, module, description, e);

        }

    }

    public void logCreate(String module, String description) {
        log(ActivityAction.CREATE, module, description);
    }

    public void logUpdate(String module, String description) {
        log(ActivityAction.UPDATE, module, description);
    }

    public void logDelete(String module, String description) {
        log(ActivityAction.DELETE, module, description);
    }

    public void logStatusChange(String module, String description) {
        log(ActivityAction.STATUS_CHANGE, module, description);
    }

    private String currentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            return "system";

        }

        return authentication.getName();

    }

    /*
     * ==========================================================
     * READ / SEARCH
     * ==========================================================
     */

    public List<ActivityLog> search(String username, String module, ActivityAction action, LocalDate fromDate, LocalDate toDate) {

        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;

        LocalDateTime to = toDate != null ? toDate.atTime(23, 59, 59) : null;

        return repository.search(
                blankToNull(username),
                blankToNull(module),
                action,
                from,
                to);

    }

    public List<ActivityLog> recent() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

}
