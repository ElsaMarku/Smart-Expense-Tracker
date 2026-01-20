package com.nirtech.SmartExpenseTracker.dto;

public class UserResponseDTO {
    private int id;
    private String username;
    private String token;

    public UserResponseDTO(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getToken() { return token; }

    public void setToken(String token) {
        this.token = token;
    }
}