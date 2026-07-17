package com.example.buy_tickets.models;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;       
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
            
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String pass) {
        this.password = pass;
            
    }
}