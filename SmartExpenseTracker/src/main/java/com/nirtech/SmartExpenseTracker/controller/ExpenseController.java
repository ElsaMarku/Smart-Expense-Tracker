package com.nirtech.SmartExpenseTracker.controller;

import com.nirtech.SmartExpenseTracker.dto.ExpenseDTO;
import com.nirtech.SmartExpenseTracker.dto.ExpenseResponseDTO;
import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.service.ExpenseService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    // SpotBugs sometimes flags constructor injection as EI_EXPOSE_REP2 (false positive in Spring)
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring-managed dependency injection; safe to store service reference.")
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ExpenseResponseDTO> addExpense(@PathVariable int userId,
                                                         @RequestBody ExpenseDTO dto) {
        Expense saved = expenseService.addExpense(dto, userId);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByUser(@PathVariable int userId) {
        List<ExpenseResponseDTO> dtos = expenseService.getExpensesByUser(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ExpenseResponseDTO toResponse(Expense e) {
        return new ExpenseResponseDTO(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getDate(),
                e.getCategory()
        );
    }
}