package com.naim.school.salary;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.naim.school.sms.BaseEntity;
import com.naim.school.teacher.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SQLDelete(sql = "UPDATE salary_ledgers SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "salary_ledgers", uniqueConstraints = @UniqueConstraint(name = "uk_salary_teacher_month", columnNames = {"teacher_id", "salary_month"}))
@Getter
@Setter
@NoArgsConstructor
public class SalaryLedger extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "salary_month", nullable = false)
    @NotNull(message = "Salary month is required")
    private LocalDate salaryMonth;

    @DecimalMin(value = "0.00", message = "Basic salary cannot be negative")
    @Column(name = "basic_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Allowances cannot be negative")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal allowances = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Deductions cannot be negative")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;

    @OneToMany(mappedBy = "salaryLedger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalaryPayment> payments = new ArrayList<>();

    public BigDecimal getNetSalary() {
        return nz(basicSalary).add(nz(allowances)).subtract(nz(deductions));
    }

    public BigDecimal getPaidAmount() {
        return payments.stream().map(SalaryPayment::getAmount).map(SalaryLedger::nz)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDueAmount() {
        BigDecimal due = getNetSalary().subtract(getPaidAmount());
        return due.signum() < 0 ? BigDecimal.ZERO : due;
    }

    public String getStatus() {
        if (getPaidAmount().signum() == 0) return "PENDING";
        if (getDueAmount().signum() == 0) return "PAID";
        return "PARTIAL";
    }

    private static BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
