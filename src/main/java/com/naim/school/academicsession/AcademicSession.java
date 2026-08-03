package com.naim.school.academicsession;

import java.time.LocalDate;

import com.naim.school.sms.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "academic_sessions")
public class AcademicSession extends BaseEntity {

    @NotBlank(message = "Session Name is required")
    @Column(nullable = false, unique = true, length = 20)
    private String sessionName;

    @NotNull(message = "Start Date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    // NULL until session is closed
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean currentSession = false;

    @Column(length = 500)
    private String description;

}