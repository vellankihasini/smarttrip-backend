
package com.smarttrip.service;

import com.smarttrip.entity.Expense;
import com.smarttrip.entity.Trip;
import com.smarttrip.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BudgetService {

    private final ExpenseRepository expenseRepository;
    private final TripService tripService;

    public BudgetService(ExpenseRepository expenseRepository,
                         TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.tripService = tripService;
    }

    public List<Expense> getExpenses(String email, Long tripId) {
        tripService.getOwnedTrip(email, tripId);
        return expenseRepository.findByTripId(tripId);
    }

    public Expense addExpense(String email, Long tripId, Expense expense) {
        Trip trip = tripService.getOwnedTrip(email, tripId);
        expense.setTrip(trip);
        return expenseRepository.save(expense);
    }

    public void deleteExpense(String email, Long tripId, Long expenseId) {
        tripService.getOwnedTrip(email, tripId);

        Expense expense = expenseRepository
                .findByIdAndTripId(expenseId, tripId)
                .orElseThrow(() -> new org.springframework.web.server
                        .ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Expense not found"));

        expenseRepository.delete(expense);
    }
}
