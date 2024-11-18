package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    String registerUser(User user);
    boolean loginUser(@Valid String username, String password);
    boolean verifyEmail(String token, String username);
    boolean resetPassword(String email, String token, String newPassword);
    boolean sendResetPasswordToken(String email);
}
