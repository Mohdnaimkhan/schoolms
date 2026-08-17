package com.naim.school.expense;

import com.naim.school.sms.BusinessException;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repository;

    public List<Expense> getAll() {

        return repository.findAllByOrderByExpenseDateDesc();

    }

    public List<Expense> getByDateRange(LocalDate startDate, LocalDate endDate) {

        return repository.findByExpenseDateBetweenOrderByExpenseDateDesc(startDate, endDate);

    }

    public Expense getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Expense not found."));

    }

    public void save(Expense expense) {

        repository.save(expense);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }

}
