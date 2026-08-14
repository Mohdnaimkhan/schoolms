package com.naim.school.certificate;

import java.time.LocalDate;

import com.naim.school.sms.BaseEntity;
import com.naim.school.studentsession.StudentSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * A Transfer Certificate is linked to the StudentSession the student was in
 * at the time of leaving, so it always reflects the correct class/section
 * for that academic year - consistent with how Result and Fee link to
 * StudentSession rather than Student directly.
 */
@Getter
@Setter
@Entity
@Table(name = "transfer_certificates")
public class TransferCertificate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_session_id", nullable = false)
    private StudentSession studentSession;

    @NotBlank(message = "TC Number is required")
    @Column(name = "tc_number", nullable = false, unique = true, length = 30)
    private String tcNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate = LocalDate.now();

    @Column(name = "date_of_leaving", nullable = false)
    private LocalDate dateOfLeaving;

    @Column(name = "reason_for_leaving", length = 300)
    private String reasonForLeaving;

    @Column(length = 50)
    private String conduct = "Good";

    @Column(name = "qualified_for_promotion", nullable = false)
    private Boolean qualifiedForPromotion = true;

    @Column(length = 500)
    private String remarks;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long studentSessionId;

}
