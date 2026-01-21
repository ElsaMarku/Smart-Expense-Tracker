package com.nirtech.SmartExpenseTracker.service;

import com.nirtech.SmartExpenseTracker.dto.ExpenseDTO;
import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.repository.ExpenseRepository;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceOtherTest {

    // Mock dependencies
    private final ExpenseRepository expenseRepository = mock(ExpenseRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);

    // Inject mocks into ExpenseService
    private final ExpenseService expenseService =
            new ExpenseService(expenseRepository, userRepository, modelMapper);

    // ----------------------
    // Test case: successful expense creation
    // ----------------------
    @Test
    void testAddExpense_Success() {

        int userId = 1;
        User user = new User();
        user.setId(userId);

        ExpenseDTO dto = new ExpenseDTO();
        dto.setTitle("Groceries");
        dto.setAmount(120.0f);
        dto.setDate(LocalDate.now());
        dto.setCategory("Food");

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Call the method
        Expense result = expenseService.addExpense(dto, userId);

        // Assertions
        assertNotNull(result);
        assertEquals("Groceries", result.getTitle());
        assertEquals(120.0f, result.getAmount());
        assertEquals("Food", result.getCategory());
        assertEquals(user, result.getUser());
    }

    // ----------------------
    // Test case: user not found
    // ----------------------
    @Test
    void testAddExpense_UserNotFound() {

        ExpenseDTO dto = new ExpenseDTO();
        dto.setTitle("Test");
        dto.setAmount(50f);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> expenseService.addExpense(dto, 99));

        assertEquals("User with Id 99 not found", exception.getMessage());
    }
}
