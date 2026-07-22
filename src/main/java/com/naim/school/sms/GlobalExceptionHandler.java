package com.naim.school.sms;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex,
                                         Model model) {

        model.addAttribute(Constants.ERROR, ex.getMessage());

        return "error/error";

    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex,
                                  Model model) {

        model.addAttribute(
                Constants.ERROR,
                "Something went wrong. Please try again."
        );

        return "error/error";

    }

}