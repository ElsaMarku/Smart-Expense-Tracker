package com.nirtech.SmartExpenseTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private float amount;
    private LocalDate date;
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Custom constructor instead of Lombok @AllArgsConstructor
    public Expense(int id, String title, float amount, LocalDate date, String category, User user) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user = user == null ? null : new User(user);
    }

    public void setUser(User user) {
        this.user = user == null ? null : new User(user);
    }

    public User getUser() {
        return user == null ? null : new User(user);
    }

    public static class ExpenseBuilder {
        private User user;

        public ExpenseBuilder user(User user) {
            this.user = user == null ? null : new User(user);
            return this;
        }
    }
}