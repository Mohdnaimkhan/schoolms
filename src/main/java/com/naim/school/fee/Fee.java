package com.naim.school.fee;

import com.naim.school.feehead.FeeHead;
import com.naim.school.sms.BaseEntity;
import com.naim.school.studentsession.StudentSession;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 * A Fee payment is linked to a StudentSession (not Student directly) so a
 * payment recorded while the student was in a particular class/section for
 * a particular academic year stays permanently tied to that placement -
 * consistent with how Result links to StudentSession. The FeeHead (Tuition
 * Fee, Transport Fee, etc.) is a proper master-data reference rather than
 * free text, so it can be matched against a FeeStructure to know what was
 * actually due.
 */
@Getter
@Setter
@Entity
@Table(
        name = "fees",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "receipt_no"
                        }
                )
        }
)
public class Fee extends BaseEntity {

    @Column(name = "receipt_no",
            nullable = false,
            unique = true,
            length = 20)
    @NotBlank(message = "Receipt number is required.")
    private String receiptNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_session_id", nullable = false)
    private StudentSession studentSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_head_id", nullable = false)
    private FeeHead feeHead;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Paid amount is required.")
    @DecimalMin(value = "0.00", message = "Paid amount cannot be negative.")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dueAmount = BigDecimal.ZERO;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeStatus status = FeeStatus.PENDING;

    @Column(length = 255)
    private String remarks;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long studentSessionId;

    @Transient
    private Long feeHeadId;

}
