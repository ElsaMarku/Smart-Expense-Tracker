package com.nirtech.SmartExpenseTracker.dto;

import java.time.LocalDate;

public class ExpenseResponseDTO {

    private int id;
    private String title;
    private float amount;
    private LocalDate date;
    private String category;
    private String username; // optional field

    // Existing constructor (6 params)
    public ExpenseResponseDTO(int id, String title, float amount, LocalDate date,
                              String category, String username) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.username = username;
    }

    // ✅ ADD THIS constructor (5 params)
    public ExpenseResponseDTO(int id, String title, float amount, LocalDate date,
                              String category) {
        this(id, title, amount, date, category, null);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public float getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public String getUsername() { return username; }
}