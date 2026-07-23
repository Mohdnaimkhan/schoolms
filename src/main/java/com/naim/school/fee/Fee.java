package com.naim.school.fee;


import com.naim.school.academicsession.AcademicSession;
import com.naim.school.student.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class Fee extends com.naim.school.sms.BaseEntity {

    @Column(name = "receipt_no",
            nullable = false,
            unique = true,
            length = 20)
    @NotBlank(message = "Receipt number is required.")
    private String receiptNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required.")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    @NotNull(message = "Academic session is required.")
    private AcademicSession academicSession;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Fee type is required.")
    private String feeType;

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

}
