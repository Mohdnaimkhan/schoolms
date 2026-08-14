package com.naim.school.salary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryLedgerRepository extends JpaRepository<SalaryLedger, Long> {
    List<SalaryLedger> findAllByOrderBySalaryMonthDesc();
    List<SalaryLedger> findByTeacherIdOrderBySalaryMonthDesc(Long teacherId);
    List<SalaryLedger> findBySalaryMonthBetweenOrderBySalaryMonthDesc(LocalDate from, LocalDate to);
    Optional<SalaryLedger> findByTeacherIdAndSalaryMonth(Long teacherId, LocalDate salaryMonth);
}
