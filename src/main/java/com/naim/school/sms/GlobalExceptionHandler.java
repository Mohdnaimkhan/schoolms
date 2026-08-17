package com.naim.school.sms;

import java.net.URI;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Expected, user-facing validation/business errors: safe to show as-is.
    // Sent back to whichever page the request came from (usually the form
    // the person was filling in) with the message as a toast, instead of
    // stranding them on a dead-end page that throws away what they typed.
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(Constants.ERROR, ex.getMessage());

        return "redirect:" + refererPath(request);

    }

    // Any other exception is unexpected (bug, DB error, null pointer, etc.).
    // Never show its raw message to the user - log it server-side instead.
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

        log.error("Unhandled exception", ex);

        redirectAttributes.addFlashAttribute(
                Constants.ERROR,
                "Something went wrong. Please try again."
        );

        return "redirect:" + refererPath(request);

    }

    /**
     * Where to send the person back to. Uses the Referer header (the page
     * they submitted from) so they land back on the form instead of a
     * generic error page - but only the path+query is trusted, never the
     * scheme/host from the header, so this can never redirect off-site
     * even if the header is unusual.
     */
    private String refererPath(HttpServletRequest request) {

        String referer = request.getHeader("Referer");

        if (referer == null || referer.isBlank()) {

            return "/";

        }

        try {

            URI uri = URI.create(referer);

            String path = uri.getRawPath();

            if (path == null || path.isBlank()) {

                return "/";

            }

            String query = uri.getRawQuery();

            return query != null ? path + "?" + query : path;

        } catch (Exception e) {

            return "/";

        }

    }

}
