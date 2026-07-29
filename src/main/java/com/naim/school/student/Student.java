package com.naim.school.student;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "students", uniqueConstraints = {
            @UniqueConstraint(columnNames = "admission_no"),
            @UniqueConstraint(columnNames = "aadhar_number")
})
public class Student extends BaseEntity {

      /*
       * ==========================================================
       * ADMISSION INFORMATION
       * ==========================================================
       */

      @Column(name = "admission_no", nullable = false, unique = true, length = 30)
      private String admissionNo;

      @Column(name = "roll_number", length = 20)
      private String rollNumber;

      @NotNull(message = "Academic session is required")
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "academic_session_id", nullable = false)
      private AcademicSession academicSession;

      @NotNull(message = "Class is required")
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "classroom_id", nullable = false)
      private ClassRoom classRoom;

      @NotNull(message = "Admission date is required")
      @Column(name = "admission_date", nullable = false)
      private LocalDate admissionDate;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private AdmissionType admissionType = AdmissionType.NEW;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private StudentStatus status = StudentStatus.ACTIVE;

      /*
       * ==========================================================
       * STUDENT INFORMATION
       * ==========================================================
       */

      @NotBlank(message = "Student name is required")
      @Size(min = 3, max = 150, message = "Name must be between 3 and 150 characters")
      @Column(nullable = false, length = 150)
      private String fullName;

      @NotNull(message = "Gender is required")
      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private Gender gender;

      @NotNull(message = "Date of birth is required")
      @Column(nullable = false)
      private LocalDate dateOfBirth;

      @Size(max = 5, message = "Blood group maximum 5 characters")
      @Column(length = 5)
      private String bloodGroup;

      @Column(length = 255)
      private String photo;

      /*
       * ==========================================================
       * PARENT INFORMATION
       * ==========================================================
       */

      @NotBlank(message = "Father name is required")
      @Size(max = 150, message = "Father name maximum 150 characters")
      @Column(nullable = false, length = 150)
      private String fatherName;

      @Size(max = 150, message = "Mother name maximum 150 characters")
      @Column(length = 150)
      private String motherName;

      /*
       * ==========================================================
       * CONTACT INFORMATION
       * ==========================================================
       */

      @Pattern(regexp = "^$|^[6-9]\\d{4}\\s?\\d{5}$", message = "Enter a valid mobile number")
      @Column(length = 10)
      private String mobile;

      @Email(message = "Enter valid email address")
      @Column(length = 100)
      private String email;

      @Size(max = 500, message = "Address maximum 500 characters")
      @Column(length = 500)
      private String address;

      /*
       * ==========================================================
       * OTHER INFORMATION
       * ==========================================================
       */

      @Pattern(regexp = "^$|^\\d{4}\\s?\\d{4}\\s?\\d{4}$", message = "Enter a valid Aadhaar Number")
      @Column(length = 12)
      private String aadharNumber;

      @Column(length = 50)
      private String religion;

      @Column(length = 50)
      private String category;

      @Column(length = 500)
      private String remarks;

      @Column(nullable = false)
      private boolean active = true;

}