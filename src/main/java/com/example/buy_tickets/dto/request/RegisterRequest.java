package com.example.buy_tickets.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

// public record RegisterRequest(
//         @NotBlank String username,
//         @Email @NotBlank String email,
//         @NotBlank @Size(min = 6) String password
// ) {}

public class RegisterRequest {

    @Email(message = "email must be a valid email address")
    private String email;

    @NotNull(message = "Password is required")
    private String password ;
    @NotNull(message = "username is required")
    private String username ;

    // getters and setters
//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }      
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }      
    public void setEmail(String email) { this.email = email; }
}

