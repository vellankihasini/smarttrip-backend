
package com.smarttrip.repository;

import com.smarttrip.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripId(Long tripId);
    Optional<Expense> findByIdAndTripId(Long id, Long tripId);
}
