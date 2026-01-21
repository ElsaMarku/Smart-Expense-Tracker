package com.nirtech.SmartExpenseTracker.integration;

import com.nirtech.SmartExpenseTracker.dto.ExpenseDTO;
import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import com.nirtech.SmartExpenseTracker.service.ExpenseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExpenseServiceIntegrationTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addExpense_Integration_Success() {

        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user = userRepository.save(user);

        ExpenseDTO dto = new ExpenseDTO();
        dto.setTitle("Groceries");
        dto.setAmount(100f);
        dto.setCategory("Food");
        dto.setDate(LocalDate.now());

        Expense expense = expenseService.addExpense(dto, user.getId());

        assertNotNull(expense.getId());
        assertEquals("Groceries", expense.getTitle());
    }
}
