package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please verify your email.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody Map<String, String> loginDetails) {
        try {
            // Retrieve and parse the role
            String roleStr = loginDetails.get("role");
            User.Role role = User.Role.valueOf(roleStr.toUpperCase()); // Convert the string to enum, ignoring case

            String username = loginDetails.get("username");
            String password = loginDetails.get("password");

            boolean isAuthenticated = userService.loginUser(username, password, role);
            if (isAuthenticated) {
                return ResponseEntity.ok("User logged in successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }

        } catch (IllegalArgumentException e) {
            // Handle the case where the provided role is not valid
            return ResponseEntity.badRequest().body("Invalid role. Valid roles are PATIENT, DOCTOR, ADMIN.");
        }
    }

    @GetMapping("/verify-email")
    public boolean verifyEmail(@RequestParam("code") String verificationCode) {
        return userService.verifyEmail(verificationCode);
    }

    @PostMapping("/reset-password/confirm")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        return userService.resetPassword(token, newPassword);
    }

    @PostMapping("/reset-password")
    public String sendResetPasswordToken(@RequestParam("email") String email) {
        return userService.sendResetPasswordToken(email);
    }
}
