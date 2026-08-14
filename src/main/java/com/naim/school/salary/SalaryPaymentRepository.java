package com.naim.school.salary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryPaymentRepository extends JpaRepository<SalaryPayment, Long> {
}
