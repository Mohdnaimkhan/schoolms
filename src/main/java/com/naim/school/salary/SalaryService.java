package com.naim.school.salary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naim.school.expense.Expense;
import com.naim.school.expense.ExpenseRepository;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryLedgerRepository ledgerRepository;
    private final SalaryPaymentRepository paymentRepository;
    private final TeacherRepository teacherRepository;
    private final ExpenseRepository expenseRepository;

    public List<SalaryLedger> getAll() { return ledgerRepository.findAllByOrderBySalaryMonthDesc(); }
    public List<SalaryLedger> getByTeacher(Long teacherId) { return ledgerRepository.findByTeacherIdOrderBySalaryMonthDesc(teacherId); }
    public SalaryLedger getLedger(Long id) { return ledgerRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary ledger not found.")); }
    public SalaryPayment getPayment(Long id) { return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary payment not found.")); }

    @Transactional
    public SalaryLedger saveLedger(SalaryLedger ledger, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found."));
        if (ledger.getSalaryMonth() == null) throw new RuntimeException("Salary month is required.");
        ledger.setSalaryMonth(ledger.getSalaryMonth().withDayOfMonth(1));
        ledger.setTeacher(teacher);
        if (ledger.getBasicSalary() == null) ledger.setBasicSalary(BigDecimal.ZERO);
        if (ledger.getAllowances() == null) ledger.setAllowances(BigDecimal.ZERO);
        if (ledger.getDeductions() == null) ledger.setDeductions(BigDecimal.ZERO);
        BigDecimal paidAlready = ledger.getPaidAmount();
        if (ledger.getNetSalary().compareTo(paidAlready) < 0) {
            throw new RuntimeException("Net salary cannot be lower than the amount already paid.");
        }
        ledgerRepository.findByTeacherIdAndSalaryMonth(teacherId, ledger.getSalaryMonth())
                .filter(existing -> !existing.getId().equals(ledger.getId()))
                .ifPresent(existing -> { throw new RuntimeException("Salary ledger already exists for this teacher and month."); });
        return ledgerRepository.save(ledger);
    }

    @Transactional
    public SalaryPayment savePayment(Long ledgerId, SalaryPayment payment) {
        SalaryLedger ledger = getLedger(ledgerId);
        BigDecimal oldAmount = BigDecimal.ZERO;
        if (payment.getId() != null) {
            SalaryPayment existing = getPayment(payment.getId());
            if (existing.getSalaryLedger() == null || !existing.getSalaryLedger().getId().equals(ledgerId)) {
                throw new RuntimeException("Payment does not belong to this salary ledger.");
            }
            oldAmount = existing.getAmount();
            payment.setExpense(existing.getExpense());
        }
        if (payment.getAmount() == null || payment.getAmount().signum() <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero.");
        }
        BigDecimal currentPaidExcludingThis = ledger.getPaidAmount().subtract(oldAmount);
        BigDecimal available = ledger.getNetSalary().subtract(currentPaidExcludingThis);
        if (available.signum() <= 0 || payment.getAmount().compareTo(available) > 0) {
            throw new RuntimeException("Payment cannot be greater than the remaining salary due (" + available.max(BigDecimal.ZERO) + ").");
        }
        if (payment.getPaymentDate() == null) payment.setPaymentDate(LocalDate.now());
        payment.setSalaryLedger(ledger);

        Expense expense = payment.getExpense();
        if (expense == null) expense = new Expense();
        expense.setCategory("Teacher Salary");
        expense.setDescription("Salary payment for " + ledger.getTeacher().getTeacherName() + " - " + ledger.getSalaryMonth());
        expense.setAmount(payment.getAmount());
        expense.setExpenseDate(payment.getPaymentDate());
        expense.setPaidTo(ledger.getTeacher().getTeacherName());
        expense.setRemarks(payment.getRemarks());
        expense = expenseRepository.save(expense);
        payment.setExpense(expense);
        return paymentRepository.save(payment);
    }

    @Transactional
    public void deletePayment(Long id) {
        SalaryPayment payment = getPayment(id);
        if (payment.getExpense() != null) expenseRepository.deleteById(payment.getExpense().getId());
        paymentRepository.delete(payment);
    }

    @Transactional
    public void deleteLedger(Long id) {
        SalaryLedger ledger = getLedger(id);
        for (SalaryPayment payment : ledger.getPayments()) {
            if (payment.getExpense() != null) expenseRepository.deleteById(payment.getExpense().getId());
        }
        ledgerRepository.delete(ledger);
    }
}
