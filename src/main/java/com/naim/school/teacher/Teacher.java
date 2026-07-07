package com.naim.school.teacher;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity {

    @NotBlank(message = "Teacher name is required")
    @Column(nullable = false)
    private String teacherName;

    @Column(unique = true)
    private String employeeCode;

    private String gender;

    @Column(length = 15)
    private String mobile;

    @Email(message = "Invalid email")
    @Column(unique = true)
    private String email;

    private String qualification;

    private Integer experience;

    private LocalDate joiningDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(length = 500)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Transient
    private Long subjectId;

    @Builder.Default
    private Boolean active = true;

}