package com.naim.school.teacher;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
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

    @Size(max = 30, message = "TEN Number cannot exceed 30 characters")
    @Pattern(regexp = "^$|^[A-Za-z0-9-]{4,30}$", message = "Enter valid TEN Number")
    @Column(name = "ten_no", length = 30, unique = true)
    private String tenNo;

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

    @Column(length = 255)
    private String specialization;

    @Column(length = 100)
    private String fatherName;

    private LocalDate dateOfBirth;

    @Column(length = 50)
    private String employmentType;

    @Column(length = 100)
    private String designation;

    @Column(length = 1000)
    private String remarks;

    @Column(length = 255)
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Transient
    private Long subjectId;

    @Builder.Default
    private Boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (teacherName != null) teacherName = teacherName.trim();
        if (employeeCode != null) employeeCode = employeeCode.trim().toUpperCase();
        if (tenNo != null) tenNo = tenNo.trim().toUpperCase();
        if (mobile != null) mobile = mobile.replaceAll("\\s+", "");
        if (email != null) email = email.trim().toLowerCase();
        if (qualification != null) qualification = qualification.trim();
        if (address != null) address = address.trim();
        if (specialization != null) specialization = specialization.trim();
        if (fatherName != null) fatherName = fatherName.trim();
        if (employmentType != null) employmentType = employmentType.trim();
        if (designation != null) designation = designation.trim();
        if (remarks != null) remarks = remarks.trim();
    }

}