package com.naim.school.student;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

   @NotNull(message = "Academic session is required.")
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "academic_session_id", nullable = false)
   private AcademicSession academicSession;

   @NotNull(message = "Classroom is required.")
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "classroom_id", nullable = false)
   private ClassRoom classRoom;

   @NotNull(message = "Admission date is required.")
   @Column(name = "admission_date", nullable = false)
   private LocalDate admissionDate;

   @NotNull(message = "Admission type is required.")
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AdmissionType admissionType = AdmissionType.NEW;

   @NotNull(message = "Student status is required.")
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private StudentStatus status = StudentStatus.ACTIVE;

   /*
    * ==========================================================
    * STUDENT INFORMATION
    * ==========================================================
    */
   @NotBlank(message = "Student name is required.")
   @Size(max = 150)
   @Column(nullable = false, length = 150)
   private String fullName;

   @NotNull(message = "Gender is required.")
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Gender gender;

   @NotNull(message = "Date of birth is required.")
   @Past(message = "Invalid date of birth.")
   @Column(nullable = false)
   private LocalDate dateOfBirth;

   @Column(length = 5)
   private String bloodGroup;

   @Column(length = 255)
   private String photo;

   /*
    * ==========================================================
    * PARENT INFORMATION
    * ==========================================================
    */

   @NotBlank(message = "Father name is required.")
   @Size(max = 150)
   @Column(nullable = false, length = 150)
   private String fatherName;

   @NotBlank(message = "Father name is required.")
   @Size(max = 150)
   @Column(length = 150)
   private String motherName;

   @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter valid mobile number.")
   @Column(length = 15)
   private String mobile;

   /*
    * ==========================================================
    * CONTACT INFORMATION
    * ==========================================================
    */

   @Email(message = "Invalid email address.")
   @Column(length = 100)
   private String email;

   @Size(max=500)
   @Column(length = 500)
   private String address;

   /*
    * ==========================================================
    * OTHER INFORMATION
    * ==========================================================
    */

   @Pattern(regexp = "^\\d{12}$", message = "Aadhaar must be 12 digits.")
   @Column(name = "aadhar_number", length = 20)
   private String aadharNumber;

   @Column(length = 50)
   private String religion;

   @Column(length = 50)
   private String category;

   @Column(length = 500)
   private String remarks;

   @Column(nullable = false)
   private Boolean active = true;

}
