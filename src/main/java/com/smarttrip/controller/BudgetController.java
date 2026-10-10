
package com.smarttrip.controller;

import com.smarttrip.entity.Expense;
import com.smarttrip.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public List<Expense> getExpenses(Authentication auth,
                                     @PathVariable Long tripId) {
        return budgetService.getExpenses(auth.getName(), tripId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense addExpense(Authentication auth,
                              @PathVariable Long tripId,
                              @RequestBody Expense expense) {
        return budgetService.addExpense(
                auth.getName(), tripId, expense);
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(Authentication auth,
                              @PathVariable Long tripId,
                              @PathVariable Long expenseId) {
        budgetService.deleteExpense(
                auth.getName(), tripId, expenseId);
    }
}
