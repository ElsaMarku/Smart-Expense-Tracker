package com.nirtech.SmartExpenseTracker.integration;

import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.repository.ExpenseRepository;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import com.nirtech.SmartExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExpenseIntegrationTest {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void expenseWorkflow_integration_totalAndBudgetWorkWithDb() {
        ExpenseService service = new ExpenseService(expenseRepository, userRepository, new ModelMapper());

        User u = User.builder()
                .username("elsa_test")
                .password("x")
                .build();
        u = userRepository.save(u);

        expenseRepository.save(Expense.builder()
                .title("A")
                .amount(10.0f)
                .date(LocalDate.now())
                .category("Food")
                .user(u)
                .build());

        expenseRepository.save(Expense.builder()
                .title("B")
                .amount(5.0f)
                .date(LocalDate.now())
                .category("Taxi")
                .user(u)
                .build());

        assertEquals(15.0, service.calculateTotalExpenses(u.getId()), 0.0001);
        assertTrue(service.checkBudgetLimit(u.getId(), 14.99));
        assertFalse(service.checkBudgetLimit(u.getId(), 15.0));
    }
}
