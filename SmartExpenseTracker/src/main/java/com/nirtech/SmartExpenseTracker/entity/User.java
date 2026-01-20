package com.nirtech.SmartExpenseTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> roles;


    public User(int id, String username, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles == null ? null : new ArrayList<>(roles);
    }


    public void setRoles(List<String> roles) {
        this.roles = roles == null ? null : new ArrayList<>(roles);
    }


    public List<String> getRoles() {
        return roles == null ? null : Collections.unmodifiableList(new ArrayList<>(roles));
    }


    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.password = other.password;
        this.roles = other.roles == null ? null : new ArrayList<>(other.roles);
    }


    public static class UserBuilder {
        private List<String> roles;

        public UserBuilder roles(List<String> roles) {
            this.roles = roles == null ? null : new ArrayList<>(roles);
            return this;
        }
    }
}