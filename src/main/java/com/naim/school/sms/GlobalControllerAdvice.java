package com.naim.school.sms;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.naim.school.schools.School;
import com.naim.school.schools.SchoolService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SchoolService schoolService;

    public GlobalControllerAdvice(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @ModelAttribute("schoolInfo")
    public School schoolInfo() {

        return schoolService.getSchool().orElse(new School());

    }

}