package com.naim.school.student;

import java.time.LocalDate;

import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(unique = true)
    private String admissionNo;

    private Integer rollNo;

    @NotBlank(message = "Student Name is required")
    @Column(nullable = false)
    private String studentName;

    @NotBlank(message = "Father Name is required")
    private String fatherName;

    private String motherName;

    private String gender;

    private LocalDate dateOfBirth;

    @Column(length = 15)
    private String mobile;

    @Email
    private String email;

    @Column(length = 500)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Transient
    private Long classId;

    private LocalDate admissionDate;

    @Builder.Default
    private Boolean active = true;

}