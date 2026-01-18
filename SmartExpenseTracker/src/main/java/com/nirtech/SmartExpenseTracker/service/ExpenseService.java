package com.nirtech.SmartExpenseTracker.service;

import com.nirtech.SmartExpenseTracker.dto.ExpenseDTO;
import com.nirtech.SmartExpenseTracker.entity.Expense;
import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.exception.ExpenseNotFoundException;
import com.nirtech.SmartExpenseTracker.exception.UserNotFoundException;
import com.nirtech.SmartExpenseTracker.repository.ExpenseRepository;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExpenseService {
     //Inject UserRepository via constructor injection.
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ModelMapper  modelMapper;
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, ModelMapper modelMapper){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    //Add Expense
    public Expense addExpense(ExpenseDTO expenseDTO, int userId){
        //Check user exist
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with Id " + userId + " not found"));

        //Map DTO to Entity - Build Pattern
        Expense expense = Expense.builder()
                .title(expenseDTO.getTitle())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .category(expenseDTO.getCategory())
                .user(user)
                .build();

        //Save expense
        return expenseRepository.save(expense);
    }

    //Update Expense
    public Expense updateExpense(int expenseId, ExpenseDTO expenseDTO) {
        //Find existing expense
        Expense existingExpense  = expenseRepository.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + expenseId + " not found"));

        //Option 1: Use Lombok @Builder + toBuilder(), need @Builder(toBuilder = true) at Expense
        //Map DTO to existingExpense - Build Pattern
//        Expense updatedExpense = existingExpense.toBuilder()
//                .title(expenseDTO.getTitle())
//                .amount(expenseDTO.getAmount())
//                .date(expenseDTO.getDate())
//                .category(expenseDTO.getCategory())
//                .build();
//        // Preserve ID and User
//        updatedExpense.setId(existingExpense.getId());
//        updatedExpense.setUser(existingExpense.getUser());

        //Option 2: Use BeanUtils.copyProperties() (Spring Utility)
            //BeanUtils.copyProperties(expenseDTO,existingExpense,"id","user");
            //expenseRepository.save(existingExpense);

        //Option 3: Use ModelMapper (External Library)
            //modelMapper.map(source, destination);
             modelMapper.map(expenseDTO, existingExpense);
        //Save expense
        return expenseRepository.save(existingExpense);
    }
    //Delete Expense
    public void deleteExpense(int expenseId){
        if(!expenseRepository.existsById(expenseId)){
            throw new ExpenseNotFoundException("Expense with ID " + expenseId + " not found");
        }

        expenseRepository.deleteById(expenseId);
    }

    //Get expenses for the user
    public List<Expense> getExpensesByUser(int userId){
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return expenseRepository.findByUserId(userId);

    }
    // --- Added for SWE303 testing: numerical output + clear decision logic ---
// Reason: the original project did not include total/budget calculations,
// and SWE303 requires BVT + equivalence + coverage on numeric ranges.

    public double calculateTotalExpenses(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream().mapToDouble(e -> e.getAmount()).sum(); // <-- lambda (float -> double)
    }

    public boolean checkBudgetLimit(int userId, double budget) {
        if (budget < 0) throw new IllegalArgumentException("Budget must be >= 0");
        double total = calculateTotalExpenses(userId);
        return total > budget;
    }
}
