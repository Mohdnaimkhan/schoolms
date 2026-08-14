package com.naim.school.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService service;

    @GetMapping
    public String list(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Model model) {

        model.addAttribute("pageTitle", "Expenses");

        List<Expense> expenses = (fromDate != null && toDate != null)
                ? service.getByDateRange(fromDate, toDate)
                : service.getAll();

        model.addAttribute("expenses", expenses);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        java.time.YearMonth month = java.time.YearMonth.now();
        java.util.List<Expense> monthExpenses = service.getByDateRange(month.atDay(1), month.atEndOfMonth());
        BigDecimal monthTotal = monthExpenses.stream().map(Expense::getAmount).filter(a -> a != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalAmount", total);
        model.addAttribute("expenseCount", expenses.size());
        model.addAttribute("expenseThisMonthCount", monthExpenses.size());
        model.addAttribute("expenseThisMonthAmount", monthTotal);

        return "expense/list";

    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Expense");

        model.addAttribute("expense", new Expense());

        return "expense/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("pageTitle", "Edit Expense");

        model.addAttribute("expense", service.getById(id));

        return "expense/form";

    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Expense expense, BindingResult result) {

        if (result.hasErrors()) {

            return "expense/form";

        }

        service.save(expense);

        return "redirect:/expenses";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "redirect:/expenses";

    }

}
