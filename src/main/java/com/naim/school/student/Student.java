package com.naim.school.student;

import java.time.LocalDate;

import com.naim.school.sms.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "photo")
@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "admission_no"),
                @UniqueConstraint(columnNames = "aadhaar_number")
        },
        indexes = {
                @Index(name = "idx_student_name", columnList = "student_name"),
                @Index(name = "idx_mobile_number", columnList = "mobile_number"),
                @Index(name = "idx_admission_no", columnList = "admission_no")
        })
public class Student extends BaseEntity {

    /*
     * ==========================================================
     * ADMISSION INFORMATION
     * ==========================================================
     */

    @Column(name = "admission_no", nullable = false, length = 30, updatable = false)
    private String admissionNo;

    @NotNull(message = "Admission Date is required")
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    /*
     * ==========================================================
     * STUDENT INFORMATION
     * ==========================================================
     */

    @NotBlank(message = "Student Name is required")
    @Size(min = 3, max = 150, message = "Student Name must be between 3 and 150 characters.")
    @Column(name = "student_name", nullable = false, length = 150)
    private String studentName;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @NotNull(message = "Date of Birth is required")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 10)
    private BloodGroup bloodGroup;

    @Column(length = 255)
    private String photo;

    /*
     * ==========================================================
     * PARENT INFORMATION
     * ==========================================================
     */

    @NotBlank(message = "Father Name is required")
    @Size(max = 150, message = "Father Name cannot exceed 150 characters.")
    @Column(name = "father_name", nullable = false, length = 150)
    private String fatherName;

    @Size(max = 150, message = "Mother Name cannot exceed 150 characters.")
    @Column(name = "mother_name", length = 150)
    private String motherName;

    @Size(max = 150, message = "Guardian Name cannot exceed 150 characters.")
    @Column(name = "guardian_name", length = 150)
    private String guardianName;

    @Size(max = 50, message = "Guardian Relation cannot exceed 50 characters.")
    @Column(name = "guardian_relation", length = 50)
    private String guardianRelation;

    /*
     * ==========================================================
     * CONTACT INFORMATION
     * ==========================================================
     */

    // Formatter : 98765 43210

    @Pattern(
            regexp = "^$|^[6-9]\\d{4}\\s?\\d{5}$",
            message = "Enter valid Mobile Number")
    @Column(name = "mobile_number", length = 11)
    private String mobileNumber;

    @Pattern(
            regexp = "^$|^[6-9]\\d{4}\\s?\\d{5}$",
            message = "Enter valid Emergency Contact")
    @Column(name = "emergency_contact", length = 11)
    private String emergencyContact;

    @Email(message = "Enter valid Email")
    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 500)
    @Column(length = 500)
    private String address;

    /*
     * ==========================================================
     * OTHER INFORMATION
     * ==========================================================
     */

    // Formatter : 1234 5678 9012

    @Pattern(
            regexp = "^$|^\\d{4}\\s?\\d{4}\\s?\\d{4}$",
            message = "Enter valid Aadhaar Number")
    @Column(name = "aadhaar_number", length = 14)
    private String aadhaarNumber;

    @Pattern(regexp = "^$|^\\d{4}\\s?\\d{4}\\s?\\d{4}$", message = "PEN No. must be a valid 12-digit number")
    @Column(name = "pen_no", length = 12, unique = true)
    private String penNo;

    @Pattern(regexp = "^$|^\\d{4}\\s?\\d{4}\\s?\\d{4}$", message = "APAAR ID must be a valid 12-digit number")
    @Column(name = "apaar_id", length = 12, unique = true)
    private String apaarId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Religion religion;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Category category;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StudentStatus status = StudentStatus.ACTIVE;

    @Size(max = 500)
    @Column(length = 500)
    private String remarks;

    /*
     * ==========================================================
     * NORMALIZE DATA
     * ==========================================================
     */

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (studentName != null)
            studentName = studentName.trim();

        if (fatherName != null)
            fatherName = fatherName.trim();

        if (motherName != null)
            motherName = motherName.trim();

        if (guardianName != null)
            guardianName = guardianName.trim();

        if (guardianRelation != null)
            guardianRelation = guardianRelation.trim();

        if (email != null)
            email = email.trim().toLowerCase();

        if (address != null)
            address = address.trim();

        if (remarks != null)
            remarks = remarks.trim();

        if (mobileNumber != null)
            mobileNumber = mobileNumber.replaceAll("\\s+", "");

        if (emergencyContact != null)
            emergencyContact = emergencyContact.replaceAll("\\s+", "");

        if (aadhaarNumber != null)
            aadhaarNumber = aadhaarNumber.replaceAll("\\s+", "");

        if (penNo != null)
            penNo = penNo.replaceAll("\\s+", "").trim();

        if (apaarId != null)
            apaarId = apaarId.replaceAll("\\s+", "").trim();
    }

    public String getFormattedPenNo() {
        return formatTwelveDigitId(penNo);
    }

    public String getFormattedApaarId() {
        return formatTwelveDigitId(apaarId);
    }

    public String getFormattedAadhaarNumber() {
        return formatTwelveDigitId(aadhaarNumber);
    }

    public String getFormattedMobileNumber() {
        return formatMobileNumber(mobileNumber);
    }

    public String getFormattedEmergencyContact() {
        return formatMobileNumber(emergencyContact);
    }

    private String formatMobileNumber(String value) {
        if (value == null || value.isBlank()) return null;
        String digits = value.replaceAll("\\s+", "");
        if (digits.length() != 10) return value;
        return digits.substring(0, 5) + " " + digits.substring(5);
    }

    private String formatTwelveDigitId(String value) {
        if (value == null || value.isBlank()) return null;
        String digits = value.replaceAll("\\s+", "");
        if (digits.length() != 12) return value;
        return digits.substring(0, 4) + " " + digits.substring(4, 8) + " " + digits.substring(8, 12);
    }

}
