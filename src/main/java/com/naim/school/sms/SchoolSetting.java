package com.naim.school.sms;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "school_settings")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SchoolSetting extends BaseEntity {

    /* ===========================
       Basic Information
    =========================== */

    @NotBlank(message = "School Name is required")
    @Column(nullable = false)
    private String schoolName;

    private String schoolCode;

    private String tagline;

    private String registrationNumber;

    /* ===========================
       Branding
    =========================== */

    private String logoPath;

    private String faviconPath;

    /* ===========================
       Contact
    =========================== */

    @NotBlank(message = "Phone Number is required")
    private String phone;

    private String alternatePhone;

    @Email
    private String email;

    private String website;

    /* ===========================
       Address
    =========================== */

    @Column(length = 500)
    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;

    /* ===========================
       Academic
    =========================== */

    private String academicSession;

    private String principalName;

    /* ===========================
       System
    =========================== */

    @Builder.Default
    private String currency = "INR";

    @Builder.Default
    private String currencySymbol = "₹";

    @Builder.Default
    private String timeZone = "Asia/Kolkata";

    @Builder.Default
    private String dateFormat = "dd-MM-yyyy";

    @Builder.Default
    private String theme = "dark";

    /* ===========================
       Footer
    =========================== */

    @Column(length = 500)
    private String footerText;

    @Column(length = 500)
    private String copyrightText;

    /* ===========================
       Social Media
    =========================== */

    private String facebook;

    private String instagram;

    private String twitter;

    private String youtube;

    private String linkedin;

}