package com.naim.school.student;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;
import jakarta.persistence.*;
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

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "academic_session_id", nullable = false)
   private AcademicSession academicSession;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "classroom_id", nullable = false)
   private ClassRoom classRoom;


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


   @Column(nullable = false, length = 150)
   private String fullName;


   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Gender gender;

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

   @Column(nullable = false, length = 150)
   private String fatherName;



   @Column(length = 150)
   private String motherName;


   @Column(length = 10)
   private String mobile;

   /*
    * ==========================================================
    * CONTACT INFORMATION
    * ==========================================================
    */


   @Column(length = 100)
   private String email;

   @Column(length = 500)
   private String address;

   /*
    * ==========================================================
    * OTHER INFORMATION
    * ==========================================================
    */

   @Column(name = "aadhar_number", length = 12)
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
