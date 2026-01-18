package com.nirtech.SmartExpenseTracker.service;

import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.exception.UserNotFoundException;
import com.nirtech.SmartExpenseTracker.repository.ExpenseRepository;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        userRepository = mock(UserRepository.class);
        expenseService = new ExpenseService(expenseRepository, userRepository, new ModelMapper());
    }

    @Test
    void calculateTotalExpenses_userNotFound_throws() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> expenseService.calculateTotalExpenses(99));
    }

    @Test
    void calculateTotalExpenses_emptyList_returnsZero() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(expenseRepository.findByUserId(1)).thenReturn(List.of());

        assertEquals(0.0, expenseService.calculateTotalExpenses(1), 0.0001);
    }

    @Test
    void calculateTotalExpenses_manyExpenses_sumsCorrectly() {
        when(userRepository.existsById(1)).thenReturn(true);

        Expense e1 = Expense.builder().amount(10.0f).build();
        Expense e2 = Expense.builder().amount(5.5f).build();
        Expense e3 = Expense.builder().amount(4.5f).build();

        when(expenseRepository.findByUserId(1)).thenReturn(List.of(e1, e2, e3));

        assertEquals(20.0, expenseService.calculateTotalExpenses(1), 0.0001);
    }

    @Test
    void checkBudgetLimit_negativeBudget_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> expenseService.checkBudgetLimit(1, -0.01));
    }

    @Test
    void checkBudgetLimit_totalEqualsBudget_false() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(expenseRepository.findByUserId(1))
                .thenReturn(List.of(Expense.builder().amount(10.0f).build()));

        assertFalse(expenseService.checkBudgetLimit(1, 10.0));
    }

    @Test
    void checkBudgetLimit_totalExceedsBudget_true() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(expenseRepository.findByUserId(1))
                .thenReturn(List.of(Expense.builder().amount(10.01f).build()));

        assertTrue(expenseService.checkBudgetLimit(1, 10.0));
    }
}
