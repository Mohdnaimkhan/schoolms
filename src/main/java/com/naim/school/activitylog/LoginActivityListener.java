package com.naim.school.activitylog;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Records LOGIN / LOGIN_FAILED entries. Spring Security publishes these
 * events automatically for every authentication attempt, so this doesn't
 * need any changes to SecurityConfig's filter chain.
 */
@Component
@RequiredArgsConstructor
public class LoginActivityListener {

    private final ActivityLogService activityLogService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {

        String username = event.getAuthentication().getName();

        activityLogService.logAs(username, ActivityAction.LOGIN, "Auth", "Logged in.");

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {

        String username = event.getAuthentication().getName();

        activityLogService.logAs(username, ActivityAction.LOGIN_FAILED, "Auth", "Failed login attempt.");

    }

}
